/*
 * Created on Wed May 11 2022
 *
 * The MIT License (MIT) Copyright (c) 2022 Peterson Yuhala, Institut d'Informatique Université de
 * Neuchâtel (IIUN)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.oracle.truffle.polyt.partitioner;

import java.util.ArrayList;
import java.util.List;

import java.util.regex.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

// polyt
import com.oracle.truffle.polyt.PolyTaintInstrument;
import com.oracle.truffle.polyt.SourceFragmentConsts;
import com.oracle.truffle.polyt.TaintTracker;
import com.oracle.truffle.polyt.utils.Logger;

public class Partitioner {

    public enum FunctionType {
        TRUSTED, UNTRUSTED, NEUTRAL, UNKNOWN, UNRESOLVED_NEUTRAL, ALL
    };

    public enum TransitionType {
        OCALL, ECALL, NONE
    };

    private TaintTracker tracker;
    private List<PolyTaintFunction> trusted;
    private List<PolyTaintFunction> neutral;
    private List<PolyTaintFunction> untrusted;
    private List<PolyTaintFunction> seen;

    private String mainFile;
    private String guestLangaugeId;
    private String outputFolder;

    public static boolean globalTrusted = false;

    /**
     * Exact lines to be added to main routine.
     */
    private List<String> mainSrcLines;

    public Partitioner(String filePath, String guestLanguageId, String outputFolder) {

        this.mainFile = filePath;
        this.guestLangaugeId = guestLanguageId;
        this.outputFolder = outputFolder;
        this.mainSrcLines = new ArrayList<String>();
    }

    public Partitioner(TaintTracker tracker, String filePath, String guestLanguageId,
            String outputFolder) {
        this.tracker = tracker;
        this.trusted = new ArrayList<PolyTaintFunction>(tracker.getTaintedMethods().values());
        this.neutral = new ArrayList<PolyTaintFunction>(tracker.getNeutralMethods().values());
        this.untrusted = new ArrayList<PolyTaintFunction>(tracker.getUntrustedMethods().values());
        this.seen = new ArrayList<PolyTaintFunction>(tracker.getSeenMethods().values());
        this.mainSrcLines = new ArrayList<String>();

        this.mainFile = filePath;
        this.guestLangaugeId = guestLanguageId;
        this.outputFolder = outputFolder;

    }

    /**
     * Prints all function calls in main.
     */
    public void printMainSource() {
        String title = " +++++++++++++ Main Source ++++++++++++";
        System.out.println(title);
        System.out.println(resolveMainSource());

    }

    /**
     * Prints all the source code lines to be added to main routine.
     */
    public void printMainLines() {

    }

    /**
     * Returns the full content of the input file
     * 
     * @param fileName
     * @return
     */
    public static String readFileContentx(String fileName) {
        String content = "";
        try {
            content = Files.readString(Path.of(fileName));
        } catch (Exception e) {
            // TODO: handle exception
        }
        return content;
    }


    public static String readFileContent(String fileName) {
        String content = "";
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(fileName));
            String line = reader.readLine();
            while (line != null) {
                // minify line here
                // line = minifyJS(line);
                content += line;
                line = reader.readLine();

            }
            reader.close();
        } catch (Exception e) {
            // TODO: handle exception
        }



        return content;
    }

    /**
     * Builds one Java native image program containing all the code to be executed.
     */
    public void buildFullNativeImage() {
        printBuildInfo(mainFile, guestLangaugeId, outputFolder);
        Path outputDir = Paths.get(outputFolder);
        CodeWriter writer = new CodeWriter();
        String source = readFileContent(mainFile);
        // System.out.println(source);

        writer.appendln(StringConstants.copyrightNotice);
        writer.appendln();
        // Add package name
        writer.appendln("package polytaint;");

        // Imports

        writer.appendln(importStatement(StringConstants.javaLangObject));
        writer.appendln(importStatement(StringConstants.currentIso));
        writer.appendln(importStatement(StringConstants.isoThread));
        writer.appendln(importStatement(StringConstants.cEntryPoint));
        writer.appendln(importStatement(StringConstants.cFunction));

        writer.appendln(importStatement(StringConstants.polyglotx));
        writer.appendln(importStatement(StringConstants.proxyx));
        writer.appendln(importStatement(StringConstants.polyglotx));
        writer.appendln();

        // Class def
        writer.appendln("public class Trusted {");
        writer.appendln();
        writer.indent();
        // writer.indents().appendln("public static Context globalContext;");
        // addStaticTryCatchBlock(writer, "globalContext =
        // Context.newBuilder().allowAllAccess(true).build();");

        // Main method
        writer.indents().appendln("public static void main(String[] args) {");
        writer.indent();
        // writer.indents().appendln("System.out.println(\"Hello Java from Code
        // builder!!\");");
        // Evaluate code in ctx
        // test code
        // writer.indents()
        // .appendln(" Value ret = ctx.eval(\"js\", \"function hello(){print('Hello
        // javascript!');}hello();\");");

        String contextInit =
                "Context globalContext = Context.newBuilder().allowAllAccess(true).build();";
        String evalCode = "Value ret = globalContext.eval(" + quoteString(guestLangaugeId) + ","
                + quoteString(minifySource(source)) + ");";
        writer.indents().appendln(contextInit);
        writer.indents().appendln(evalCode);

        writer.outdent();
        writer.indents().appendln("}"); // close main method
        writer.outdent();
        // TODO all entry points
        writer.indents().appendln("}");// close class def
        writer.writeFile(StringConstants.TRUSTED_IMG, outputDir);

    }

    /**
     * Build two native images using the taint tracking results.
     */
    public void partitionApplication() {

        // Generate Multifunction class in and out
        // generateMultiFunctionClass(true);
        // generateMultiFunctionClass(false);

        // Generate reflection config files
        generateReflectConfig(true);
        generateReflectConfig(false);

        // Generate Parameter classes
        // for (PolyTaintFunction func : seen) {
        // if (func.getArgumentTypes().size() > 0) {
        // generateParamClass(func);
        // }
        // }

        /**
         * For trusted image
         */
        buildImage(true);
        generateProxyRoutines(true);
        generateProxyHeaders(true);

        /**
         * For untrusted image
         */
        buildImage(false);
        generateProxyRoutines(false);
        generateProxyHeaders(false);

        /**
         * Build EDL file
         */
        generateEdlFile();
    }

    /**
     * Build enclave native image program.
     */
    public void buildImage(boolean isTrustedImg) {
        // printBuildInfo(mainFile, guestLangaugeId, outputFolder);
        Path outputDir = Paths.get(outputFolder);
        CodeWriter writer = new CodeWriter();

        // Write class code

        String className = isTrustedImg ? "Trusted" : "Untrusted";
        // String multiFunClass = isTrustedImg ? "MultiFunctionIn" : "MultiFunctionOut";

        writer.appendln(StringConstants.copyrightNotice);
        writer.appendln();

        // Add package name
        writer.appendln("package polytaint;");

        // Imports

        writer.appendln(importStatement(StringConstants.currentIso));
        writer.appendln(importStatement(StringConstants.isoThread));
        writer.appendln(importStatement(StringConstants.cEntryPoint));
        writer.appendln(importStatement(StringConstants.cFunction));
        writer.appendln(importStatement(StringConstants.ccharpointer));

        writer.appendln(importStatement(StringConstants.polyglotx));
        writer.appendln(importStatement(StringConstants.proxyx));
        writer.appendln(importStatement(StringConstants.polyglotx));

        writer.appendln(importStatement(StringConstants.serializer));
        writer.appendln(importStatement(StringConstants.javaList));
        writer.appendln(importStatement(StringConstants.polyglotx));

        writer.appendln();



        // Class def
        writer.appendln("public class " + className + " {");
        writer.appendln();
        writer.indent();
        // writer.indents().appendln("public static Context globalContext;");
        // addStaticTryCatchBlock(writer,
        // "globalContext = Context.newBuilder().allowAllAccess(true).build();");
        // writer.appendln();
        // /**
        // * Instantiate global MultiFunction object. This object essentially exposes all the static
        // * methods in the Java host scope so they can be accessible in the guest language context
        // * (e.g JS, Ruby etc)
        // */
        // writer.indents().appendln("public static " + multiFunClass + " multiFunc = new "
        // + multiFunClass + "(globalContext);");
        // writer.appendln();

        // Add main method if untrusted image

        /**
         * Add list types used for serializing arrays
         */
        writer.indents().appendln(StringConstants.createSimpleList);
        writer.indents().appendln(StringConstants.createDoubleList);

        if (isTrustedImg) {
            buildDummyMain(writer);
        } else {
            buildMain(writer);
        }

        if (isTrustedImg) {
            // build java static methods for all methods
            globalTrusted = true;
            buildStaticMethods(trusted, writer, false);
            buildStaticMethods(neutral, writer, false);
            buildStaticMethods(untrusted, writer, true);

            // add entry points for trusted methods
            buildEntryPoints(trusted, writer);

            /**
             * Calling untrusted methods from trusted enclave will require a transition. Add native
             * header declarations for the transition function/proxy. The proxy itself doesn't
             * perform the transition but relays this to the real C transition code.
             */
            addProxyHeaders(untrusted, writer);
        } else {
            // build java static methods for all methods
            globalTrusted = false;
            buildStaticMethods(trusted, writer, true);
            buildStaticMethods(neutral, writer, false);
            buildStaticMethods(untrusted, writer, false);

            // add entry points for untrusted methods
            buildEntryPoints(untrusted, writer);

            /**
             * Calling trusted methods inside untrusted image will require a transition. Add native
             * header declarations for the transition function/proxy. The proxy itself doesn't
             * perform the transition but relays this to the real C transition code.
             */
            addProxyHeaders(trusted, writer);
        }

        writer.outdent();
        writer.indents().appendln("}");// close class def

        String fileName =
                isTrustedImg ? StringConstants.TRUSTED_IMG : StringConstants.UNTRUSTED_IMG;
        writer.writeFile(fileName, outputDir);
    }

    /**
     * Build an empty main function for the Trusted image. The real main entry point is located in
     * the Untrusted image. However for "build reasons", it seems we need a main entry point in each
     * application.
     * 
     * @param writer
     */
    public void buildDummyMain(CodeWriter writer) {

        // Main method
        writer.indents().appendln("public static void main(String[] args) {");
        writer.indent();
        writer.indents().appendln("System.out.println(\"Trusted dummy main!!\");");
        writer.outdent();
        writer.indents().appendln("}"); // close main method
    }

    /**
     * Build the main function. NB: the main function is located in the Untrusted Image.
     * 
     * @param writer
     */
    public void buildMain(CodeWriter writer) {

        /**
         * The main entry point is always in the untrusted class (by convention).
         */
        String mainClass = "Untrusted.class";

        // Main method
        writer.indents().appendln("public static void main(String[] args) {");
        writer.indent();
        writer.indents().appendln("System.out.println(\"Main: untrusted partition!!\");");
        // create context
        String createContext =
                "Context context = Context.newBuilder().allowAllAccess(true).build();";
        writer.indents().appendln(createContext);
        // expose functions as truffle values
        // expose functions as truffle values
        for (PolyTaintFunction polyFunc : seen) {
            // skip func definition; the wrapper will contain it
            String pfName = polyFunc.getFuncSimpleName();


            String line = "Value " + pfName + " = context.asValue(" + mainClass
                    + ").getMember(\"static\").getMember(\"" + pfName + "\");";
            writer.indents().appendln(line);
        }



        String mainInvocation = "context.eval(\"" + getGuestId() + "\"," + "\"" + getWrappedMain()
                + "\").execute(" + getMainWrapperParams() + ");";

        writer.indents().appendln(mainInvocation);
        writer.outdent();
        writer.indents().appendln("}"); // close main method
    }

    /**
     * Analyzes the source code to obtain the main instructions. This works "Ok" for JS but is
     * really tricky to do correctly. So many regexes and assumptions done for now e.g all your
     * source is separated by semicolons etc
     * 
     * Simply put, all code not within a function block (e.g function in JS, def in Python) is
     * considered main source code.
     */
    public String resolveMainSource() {

        String mainSource = "";
        String mini = minifySource(readFileContent(mainFile));
        /**
         * Find all substrings beginning with "function", anything between and ending with "}". i.e
         * all js function definitions. function funcName(param){print(param);}
         */

        Logger.log("Minified source: " + mini);

        switch (PolyTaintInstrument.primaryGuest) {
            case JS:
                /**
                 * This removes all function definitions from the main file source code content.
                 * What we call the "main source is what is left" out of the function definitions;
                 * the function definitions will be exported accordingly as Java static methods and
                 * exposed in the polyglot execution context.
                 */
                // String jsPattern = "function[^}]*}";//Pyuhala: does not work correctly
                // mainSource = mini.replaceAll(jsPattern, "");

                /*
                 * JS functions can't be easily obtained with regexes so we do it manually with the
                 * below method.
                 */
                mainSource = getIsolatedLines(mini);
                break;

            case PYTHON:
                /**
                 * It's not simple to extract python function definitions with regular expressions.
                 * So for the purpose of our POC, we add a "magic string" after each python function
                 * definition. This string makes extraction of the function definitions easier with
                 * regular expressions.
                 */
                String pythonPathern = "def[^}]*" + SourceFragmentConsts.PYTHON_FUNC_END;
                mainSource = mini.replaceAll(pythonPathern, "");

                break;

            default:
                break;
        }
        return mainSource;
    }

    /**
     * PYuhala: Gets all parts of the string which are not within function blocks.
     * 
     * @param src
     */
    public String getIsolatedLines(String src) {
        // Source strings used by algorithm
        String function = "function";
        String left = "{";
        String right = "}";

        int srcLen = src.length();
        Logger.log("Getting isolated lines: minified source length = " + srcLen);

        String isolateLines = "";

        // stage 1: find all indices of "function" in the src string
        List<Integer> indexes = new ArrayList<Integer>();
        int index = 0;
        while (index != -1) {
            index = src.indexOf(function, index);
            if (index != -1) {
                indexes.add(index);
                index++;
            }
        }

        // Logger.log("Function indexes in minified source: " + indexes.toString());
        // number of left braces in substring
        int leftCount = 0;
        // number of right braces in substring
        int rightCount = 0;
        // current in src string
        int curr = 0;


        /**
         * stage 2: starting from all function indexes, find the outermost matching curly braces, We
         * do this by counting the number of left and right braces in the substring. The moment they
         * are equal we should have found the largest block. Add all lines between this last brace
         * to the next function block.
         */

        int idx = 0;

        // Add lines between src start to first function index exclusive
        int firstFuncIndex = indexes.get(0);
        String firstBlock = src.substring(0, firstFuncIndex);
        String midBlock = "";
        isolateLines += firstBlock;

        // Get all blocks not within function bodies
        for (int i = 0; i < indexes.size(); i++) {
            idx = indexes.get(i);
            leftCount = 0;
            rightCount = 0;
            curr = idx;
            // get the first left brace
            String schar = Character.toString(src.charAt(curr));
            while (!schar.equals(left)) {
                // Logger.log
                curr++;
                schar = Character.toString(src.charAt(curr));
                if (curr == src.length() - 1) {
                    break;
                }
            }
            // curr is now at first left brace
            // Logger.log(">>>>>>> Found brace at index: " + curr);
            leftCount++;
            while (leftCount != rightCount) {
                curr++;
                if (curr == src.length()) {
                    break;
                } else {
                    schar = Character.toString(src.charAt(curr));
                }

                if (schar.equals(left)) {
                    leftCount++;
                }
                if (schar.equals(right)) {
                    rightCount++;
                }

            }
            /**
             * curr is now at closing function brace, get block from this curr index to next
             * function index
             */
            if (i < indexes.size() - 1) {
                midBlock = src.substring(curr + 1, indexes.get(i + 1));
                isolateLines += midBlock;
            } else {
                // treat last block at index(size -1) differently
                midBlock = src.substring(curr + 1, srcLen - 1);
                isolateLines += midBlock;
            }



        }

        /**
         * The last semicolon seems to not be included for some reason. Using this shortcut for now;
         * one more semicolon doesn't hurt.
         */
        return isolateLines + ";";

    }

    /**
     * Exposes the method objects exported in the context in the python scope so they can be used as
     * param to the main wrapper.
     */
    public void exposeMethodsInPythonScope(String wrappedFunc) {

    }

    public String getWrappedMain() {
        String body = "";

        switch (PolyTaintInstrument.primaryGuest) {
            case JS:
                body += "function main_wrapper(" + getMainWrapperParams() + "){";

                // Add main instructions
                body += resolveMainSource() + "}";
                // invoke the main wrapper function.
                body += "main_wrapper;";
                break;
            case PYTHON:
                body += "def main_wrapper(" + getMainWrapperParams() + "):" + "\n";

                // Add main instructions
                body += "\t" + resolveMainSource() + "\n";
                // invoke the main wrapper function.
                body += "\nmain_wrapper";

                // minify body
                break;
            default:
                break;
        }

        return body;

    }

    /**
     * Build java static function for the corresponding method The function is a public static java
     * method. The idea behind its logic is to provide a way to call the corresponding guest
     * language (e.g JS) method from within a java program. A multifunction object contains all the
     * "executable truffle values" for the other functions (b/c it may call them), as well as a
     * parameter object which holds the parameters. An englobing parent function is defined within
     * which all the seen functions are declared/exposed to the guest code. The guest function
     * definition is also added (got from it AST node) and is called within this parent function.
     * The parent function is then invoked.
     * 
     * If the method does not exist in that runtime, a transition is done via a proxy method.
     */
    public void buildStaticMethods(List<PolyTaintFunction> methods, CodeWriter writer,
            boolean doTransitions) {
        String methodBody = "";
        String invocation = "";

        for (PolyTaintFunction func : methods) {

            String signature = "public static" + StringConstants.space + func.getReturnType()
                    + StringConstants.space + func.getFuncSimpleName()
                    + func.getParamSignature(false);

            methodBody = "";


            /**
             * If this family of static methods perform transitions i.e ecall or ocalls, then the
             * static method invokes the proxy routine which will handle the transition. Serialize
             * any object params here.
             */
            if (doTransitions) {

                if (func.hasObjectParam() || func.hasObjectReturn()) {
                    writer.indents().appendln(signature + " throws Exception {");
                    writer.indent();
                } else {
                    writer.indents().appendln(signature + "{");
                    writer.indent();
                }
                // get serialization code
                func.addSerializedParams(writer);

                invocation = func.getFuncSimpleName() + "_proxy" + func.getProxyCallInvocation()
                        + StringConstants.semiColon;

                String retType = func.getReturnType();
                if (!retType.equals("void")) {
                    if (func.hasObjectReturn()) {
                        // deserialize the result from the proxy and return
                        func.returnObjFromProxy(writer, invocation);

                    } else {
                        // just return if it is a primitive type
                        invocation = "return " + invocation;
                        writer.indents().appendln(invocation);

                    }
                } else {
                    writer.indents().appendln(invocation);
                }



                // String body = func.getReturnType().equals("void") ? methodBody : "return " +
                // methodBody;
                // writer.indents().appendln(body);
            } else {
                /**
                 * Otherwise use the function's AST code snippet to build an executable function
                 * literal.
                 */
                writer.indents().appendln(signature + "{");
                writer.indent();


                String mainClass = globalTrusted ? "Trusted.class" : "Untrusted.class";
                // create context
                String createContext =
                        "Context context = Context.newBuilder().allowAllAccess(true).build();";
                writer.indents().appendln(createContext);

                // expose functions as truffle values
                for (PolyTaintFunction polyFunc : seen) {
                    // skip func definition; the wrapper will contain it
                    String pfName = polyFunc.getFuncSimpleName();
                    if (pfName.equals(func.getFuncSimpleName())) {
                        continue;
                    }

                    String line = "Value " + pfName + " = context.asValue(" + mainClass
                            + ").getMember(\"static\").getMember(\"" + pfName + "\");";
                    writer.indents().appendln(line);
                }

                /**
                 * PYuhala: box all object params as values. The getWrappedParams will give name the
                 * objects correctly. That is: param_obji or parami, for object and primitive types
                 * respectively.
                 */
                List<String> args = func.getArgumentTypes();
                for (int i = 0; i < args.size(); i++) {
                    String type = args.get(i);
                    if (type.equals("Object")) {
                        String valBox = "Value param_val" + (i + 1) + " = Value.asValue(param"
                                + (i + 1) + ");";
                        writer.indents().appendln(valBox);
                    }
                }


                methodBody += "context.eval(\"" + getGuestId() + "\"," + "\""
                        + this.getWrappedMethod(func) + "\").execute(" + getWrapperParams(func)
                        + ")";

                String contextRet = func.getContextRet();
                String body = "";
                if (contextRet.equals("")) {
                    body = func.getReturnType().equals("void") ? methodBody + ";"
                            : "return " + methodBody + ";";
                } else {
                    body = func.getReturnType().equals("void") ? methodBody + ";"
                            : "return " + methodBody + "." + contextRet + ";";
                }



                writer.indents().appendln(body);


            }

            writer.outdent();
            writer.indents().appendln("}"); // close method
            writer.appendln();
        }
    }

    /**
     * Returns the string identifier of the guest language.
     * 
     * @return
     */
    public String getGuestId() {
        return this.guestLangaugeId;
    }

    /**
     * Returns a code snippet where the corresponding function definition is enclosed within a
     * wrapper method wherein all seen methods are exposed to the function, and call parameters are
     * resolved.
     * 
     * 
     * @param guestId
     * @return
     */
    public String getWrappedMethod(PolyTaintFunction func) {
        String body = "";

        switch (PolyTaintInstrument.primaryGuest) {
            case JS:
                body = wrapJsMethod(func);
                break;

            case PYTHON:
                body = wrapPythonMethod(func);
                break;
            default:
                break;
        }

        return body;
    }

    /**
     * Returns the parameters of the truffle execute invocation corresponding to this function. The
     * execute invocation's parameters are the truffle values corresponding to the remaining methods
     * as well as this method's parameters.
     * 
     * @param func
     * @return
     */
    public String getWrapperParams(PolyTaintFunction func) {
        String params = "";
        String functions = "";
        // String mainClass = globalTrusted ? "Trusted.class" : "Untrusted.class";
        String separator = ",";
        String lastSeen = seen.get(seen.size() - 1).getFuncSimpleName();
        boolean funcIsLast = lastSeen.equals(func.getFuncSimpleName());


        for (int i = 0; i < seen.size(); i++) {
            String f = seen.get(i).getFuncSimpleName();

            if (f.equals(func.getFuncSimpleName())) {

                continue;
            }

            /**
             * If any of these tests is true, change the separator to empty string.
             */
            boolean test1 = (i == seen.size() - 2) && funcIsLast;
            boolean test2 = (i == seen.size() - 1);

            if (test1 || test2) {
                separator = "";
            }


            functions += f + separator;
        }
        /**
         * If atleast 1 more function was seen other than func, we have function parameters.
         */
        if (seen.size() > 1) {
            separator = ",";
        }

        List<String> argTypes = func.getArgumentTypes();

        for (int i = 0; i < argTypes.size(); i++) {
            String type = argTypes.get(i);
            if (i > 0 && !separator.equals(",")) {
                separator = ",";
            }
            /**
             * This param will be boxed in a Value object by the partitioner
             */
            if (type.equals("Object")) {
                params += separator + "param_val" + (i + 1);
            } else {
                params += separator + "param" + (i + 1);
            }

        }



        return (functions + params);
    }

    /**
     * Returns parameters to the main wrapper. These parameters are simply the corresponding truffle
     * values of the seen functions.
     * 
     * 
     * @return
     */
    public String getMainWrapperParams() {
        String functions = "";
        String separator = ",";
        for (int i = 0; i < seen.size(); i++) {
            String f = seen.get(i).getFuncSimpleName();

            if (i == seen.size() - 1) {
                separator = "";
            }

            functions += f + separator;

        }



        return functions;
    }


    /**
     * Wraps a Python method to be exposed in the native image.
     * 
     * @param func
     * @return
     */
    public String wrapPythonMethod(PolyTaintFunction func) {
        String body =
                "def " + func.getFuncSimpleName() + "_wrapper(" + getWrapperParams(func) + "):\n";

        // Add function definition itself.
        body += "\t" + minifySource(func.getMethodDefition()) + "\n";

        /**
         * Invoke function: do explicit return in case function returns a value.
         */

        if (func.getReturnType().equals(StringConstants.VOID_RET)) {
            body += "\t" + func.getFuncSimpleName() + func.getCallInvocation() + "\n";
        } else {
            body += "\t" + "return " + func.getFuncSimpleName() + func.getCallInvocation() + "\n";

        }

        // invoke wrapper function and close code snippet.
        body += "\n" + func.getFuncSimpleName() + "_wrapper" + "\n";// TODO: fix this for python

        // TODO: minify body
        return body;

    }

    /**
     * Wraps a JS method to be exposed in the native image.
     * 
     * @param func
     * @return
     */
    public String wrapJsMethod(PolyTaintFunction func) {

        String body = "function " + func.getFuncSimpleName() + "_wrapper(" + getWrapperParams(func)
                + "){";


        // Add function definition itself.
        body += minifySource(func.getMethodDefition());
        /**
         * Invoke function: do explicit return in case function returns a value.
         */

        if (func.getReturnType().equals(StringConstants.VOID_RET)) {
            body += func.getFuncSimpleName() + func.getCallInvocation() + ";}";
        } else {
            body += "return " + func.getFuncSimpleName() + func.getCallInvocation() + ";}";

        }

        // invoke wrapper function and close code snippet.
        body += func.getFuncSimpleName() + "_wrapper;";

        return body;
    }

    /**
     * This is a graalvm entry point method which is called directly by the native methods (i.e
     * ocalls/ecalls) This entrypoint method calls the corresponding static method generated above
     * with the given params as well as the current isolate thread providing the execution context.
     */
    public void buildEntryPoints(List<PolyTaintFunction> methods, CodeWriter writer) {
        String symbolName;
        String invocation = "";

        for (PolyTaintFunction func : methods) {

            /**
             * Add CEntryPoint annotation to method. The symbol name will be funcShortName_entry.
             * For example: @CEntryPoint(name = "funcShortName_entry")
             */
            symbolName = func.getFuncSimpleName() + "_entry";
            writer.indents().appendln("@CEntryPoint(name = \"" + symbolName + "\")");
            String signature = "public static" + StringConstants.space + func.getProxyReturnType()
                    + StringConstants.space + symbolName + func.getEntryPointSignature();

            /**
             * If it has an object return type, this will need to be deserialized and a ccharpointer
             * returned instead. The logic involved throws an Exception
             */
            if (func.getReturnType().equals("Object")) {
                writer.indents().appendln(signature + " throws Exception {");
                writer.indent();
            } else {
                writer.indents().appendln(signature + "{");
                writer.indent();
            }


            /**
             * Add method body: the entry point calls the corresponding static method
             */
            func.deserializeParams(writer);

            invocation = func.getFuncSimpleName() + func.getEntryCallInvocation()
                    + StringConstants.semiColon;
            /**
             * Handle return values if they exist
             */
            String retType = func.getReturnType();
            if (!retType.equals("void")) {
                if (retType.equals("Object")) {
                    // serialize return type and send ccharpointer
                    func.serializeReturnValue(writer, invocation);

                } else {
                    // just return if it is a primitive type
                    invocation = "return " + invocation;
                    writer.indents().appendln(invocation);
                }
            } else {
                writer.indents().appendln(invocation);
            }



            writer.outdent();
            writer.indents().appendln("}"); // close method
            writer.appendln();

        }
    }

    /**
     * Add declarations for the proxy native routines called by the Java static methods Our
     * convention for proxy routine names is: funcName_proxy
     */
    public void addProxyHeaders(List<PolyTaintFunction> methods, CodeWriter writer) {

        for (PolyTaintFunction func : methods) {
            String name = func.getFuncSimpleName() + "_proxy";

            String signature = "public static native " + func.getProxyReturnType()
                    + StringConstants.space + name + func.getProxySignature() + ";";

            writer.indents().appendln("@CFunction");
            writer.indents().appendln(signature);

        }
    }

    /**
     * Generate C/Cpp definitions for the proxy and transition routines. The proxy routines are
     * invoked directly by the Java static functions. The proxy then invokes the corresponding
     * ecall/ocall transition routine. That is: Java static method ---> C Proxy function --->
     * Ocall/Ecall routine --> Entry point method
     */
    public void generateProxyRoutines(boolean isTrustedImg) {
        // printBuildInfo(mainFile, guestLangaugeId, outputFolder);
        Path outputDir = Paths.get(outputFolder);
        CodeWriter writer = new CodeWriter();

        String fileName =
                isTrustedImg ? StringConstants.PROXY_IN_CPP : StringConstants.PROXY_OUT_CPP;

        /**
         * For trusted image, the proxies are for untrusted functions; while for untrusted image,
         * the proxies are for the trusted functions.
         */

        List<PolyTaintFunction> proxyMethods = isTrustedImg ? untrusted : trusted;

        /**
         * For trusted image, the definitions to be added are ecalls; while for untrusted image, the
         * definitions to be added are ocalls.
         */
        List<PolyTaintFunction> transitions = isTrustedImg ? trusted : untrusted;
        /**
         * Proxy routines do ocall transitions inside and ecalls outside
         */
        String proxyTrans = isTrustedImg ? "ocall_" : "ecall_";
        /**
         * Transition routine definitions inside are ecalls and ocalls outside
         */
        String transPrefix = isTrustedImg ? "ecall_" : "ocall_";

        /**
         * Isolate thread used to invoke entry point methods These names are the same in the sgx
         * module and should remain the same.
         */
        String iso = isTrustedImg ? StringConstants.ENC_ISO : StringConstants.APP_ISO;

        // Copy right notice
        writer.appendln(StringConstants.copyrightNotice);
        writer.appendln();

        // Includes

        addHeaderIncludes(isTrustedImg, writer);

        writer.appendln();

        // Add extern variables
        if (isTrustedImg) {
            writer.appendln("extern graal_isolatethread_t *global_enc_iso;");

        } else {
            writer.appendln("extern sgx_enclave_id_t global_eid;");
            writer.appendln("extern graal_isolatethread_t *global_app_iso;");
        }

        writer.appendln();

        // Add Cpp definitions for ecall/ocall transitions
        for (PolyTaintFunction t : transitions) {
            boolean hasObjRet = false;
            String retType = t.getReturnType();
            if (retType.equals("Object")) {
                // type = "void*";
                /**
                 * PYuhala: the object will be returned via an additional buffer parameter.
                 */
                retType = "void";
                hasObjRet = true;
            }
            writer.appendln(retType + " " + transPrefix + t.getFuncSimpleName()
                    + t.getTransitionSignature() + " {");
            writer.indent();
            if (retType.equals("void")) {
                if (hasObjRet) {
                    String getCharPtr = "char* ret = " + t.getFuncSimpleName() + "_entry"
                            + t.getEntryInvocation(iso) + ";";
                    String doMemcpy = "memcpy(retPtr,ret," + StringConstants.maxBufSize + ");";
                    writer.indents().appendln(getCharPtr);
                    writer.indents().appendln(doMemcpy);
                } else {
                    writer.indents().appendln(
                            t.getFuncSimpleName() + "_entry" + t.getEntryInvocation(iso) + ";");
                }
            } else {



                writer.indents().appendln("return " + t.getFuncSimpleName() + "_entry"
                        + t.getEntryInvocation(iso) + ";");
            }

            writer.outdent();
            writer.indents().appendln("}");

        }

        // Add Cpp definitions for proxy routines
        for (PolyTaintFunction p : proxyMethods) {

            String retType = p.getReturnType();
            if (retType.equals("Object")) {
                retType = "void*";
                // type = "void*";
                /**
                 * PYuhala: the object will be returned via an additional buffer parameter.
                 */
                retType = "void";
            }
            writer.appendln(retType + " " + p.getFuncSimpleName() + "_proxy"
                    + p.getCParamSignature() + " {");
            writer.indent();
            if (retType.equals("void")) {
                writer.indents().appendln(proxyTrans + p.getFuncSimpleName()
                        + p.getTransInvocation(!isTrustedImg) + ";");
            } else {

                writer.indents().appendln(retType + " ret;");
                writer.indents().appendln(proxyTrans + p.getFuncSimpleName()
                        + p.getTransInvocation(!isTrustedImg) + ";");

                writer.indents().appendln("return ret;");



            }

            writer.outdent();
            writer.indents().appendln("}");
        }

        writer.writeFile(fileName, outputDir);
    }

    /**
     * Add necessary header inclusions to the writer object. NB: The names of included headers or
     * external variables may change in the future, which will require few modifications in the
     * logic.
     * 
     * @param isTrusted
     * @param writer
     */
    public void addHeaderIncludes(boolean isTrusted, CodeWriter writer) {
        if (isTrusted) {
            writer.appendln(includeDirective("Proxy_In.h"));
            writer.appendln(includeDirective("checks.h"));
            writer.appendln(includeDirective("../../Enclave.h"));
            writer.appendln(includeDirective("graal_isolate.h"));
            writer.appendln(includeDirective("main.h"));
            writer.appendln("#include <stdio.h>");
            writer.appendln("#include <stdlib.h>");

        } else {
            writer.appendln(includeDirective("Proxy_Out.h"));
            writer.appendln(includeDirective("graal_isolate.h"));
            writer.appendln(includeDirective("Enclave_u.h"));
            writer.appendln(includeDirective("main.h"));
            writer.appendln("#include <stdio.h>");
            writer.appendln("#include <stdlib.h>");


        }
    }

    /**
     * Generates header files with necessary inclusions
     * 
     * @param isTrustedImg
     */
    public void generateProxyHeaders(boolean isTrustedImg) {

        /**
         * For trusted image, the proxies are for untrusted functions; while for untrusted image,
         * the proxies are for the trusted functions.
         */

        List<PolyTaintFunction> proxyMethods = isTrustedImg ? untrusted : trusted;

        String suffix = isTrustedImg ? "IN_H" : "OUT_H";

        Path outputDir = Paths.get(outputFolder);
        CodeWriter writer = new CodeWriter();

        // Open include guards
        writer.appendln("#ifndef __PROXY_" + suffix);
        writer.appendln("#define __PROXY_" + suffix);
        writer.appendln();

        // Open Extern C
        writer.appendln("#if defined(__cplusplus)");
        writer.appendln("extern \"C\" { ");
        writer.appendln("#endif");
        writer.appendln();

        // Add proxy prototypes
        for (PolyTaintFunction f : proxyMethods) {

            String type = f.getReturnType();
            if (type.equals("Object")) {
                // type = "void*";
                type = "void";
            }
            writer.appendln(
                    type + " " + f.getFuncSimpleName() + "_proxy" + f.getCParamSignature() + ";");
        }
        writer.appendln();

        // Close Extern C
        writer.appendln("#if defined(__cplusplus)");
        writer.appendln("}");
        writer.appendln("#endif");
        writer.appendln();

        // Close include guards
        writer.appendln("#endif");

        writer.appendln();

        String headerFile = isTrustedImg ? StringConstants.PROXY_IN_H : StringConstants.PROXY_OUT_H;
        writer.writeFile(headerFile, outputDir);

    }

    /**
     * Generates the EDL file corresponding to the partitioned application. This file contains
     * prototypes for the ecall/ocall transition routines. These routines then invoke the
     * corresponding C entry point methods for the functions. i.e Ocall/Ecall --> Entry point -->
     * Java static method.
     */
    public void generateEdlFile() {

        Path outputDir = Paths.get(outputFolder);
        CodeWriter writer = new CodeWriter();

        // Copy right notice
        writer.appendln(StringConstants.copyrightNotice);
        writer.appendln();

        // Open enclave block
        writer.appendln("enclave { ");
        writer.indent();

        /**
         * Add ecalls: ecalls correspond to the trusted routines.
         */
        // Open trusted block
        writer.indents().appendln("trusted {");
        writer.indent();

        for (PolyTaintFunction ecall : trusted) {
            String type = ecall.getReturnType();
            if (type.equals("Object")) {
                // type = "void*";
                type = "void";// return val is passed as a new param
            }

            writer.indents().appendln("public " + type + " ecall_" + ecall.getFuncSimpleName()
                    + ecall.getEdlSignature() + ";");


        }

        // Close trusted block
        writer.outdent();
        writer.indents().appendln("};");
        writer.appendln();

        /**
         * Add ocalls: ocalls correspond to the untrusted routines w/o main.
         */
        // Open untrusted block
        writer.indents().appendln("untrusted {");
        writer.indent();

        for (PolyTaintFunction ocall : untrusted) {
            String type = ocall.getReturnType();
            if (type.equals("Object")) {
                // type = "void*";
                type = "void";// return val is passed as a new param
            }

            writer.indents().appendln(
                    type + " ocall_" + ocall.getFuncSimpleName() + ocall.getEdlSignature() + ";");


        }

        // Close untrusted block
        writer.outdent();
        writer.indents().appendln("};");
        writer.appendln();

        // Close enclave block
        writer.outdent();
        writer.appendln("};");
        writer.appendln();

        writer.writeFile(StringConstants.POLYTAINT_EDL, outputDir);
    }


    /**
     * Generates JSON reflection configuration files to be used for native image building. The file
     * has the following structure: [ { "name":"iiun.smartc.Main",
     * "methods":[{"name":"helloRuby","parameterTypes":["int"] }] } ]
     * 
     * TODO: refactor this method and use JSON writer package.
     * 
     * @param isTrusted
     */
    public void generateReflectConfig(boolean isTrusted) {
        String fileName = isTrusted ? "reflect-config-in" : "reflect-config-out";
        Path outputDir = Paths.get(outputFolder);
        CodeWriter writer = new CodeWriter();

        String className = isTrusted ? "polytaint.Trusted" : "polytaint.Untrusted";
        String separator = ",";

        writer.appendln("[");
        // Object class is used for serialization


        writer.appendln("{");
        writer.appendln("\"name\":\"java.lang.Object\"");
        // writer.appendln("\"allDeclaredConstructors\" : true,");
        // writer.appendln("\"allPublicConstructors\" : true,");
        // writer.appendln("\"allDeclaredMethods\" : true,");
        // writer.appendln("\"allPublicMethods\" : true");
        writer.appendln("},");


        writer.appendln("{");
        String name = "\"name\":\"" + className + "\",";
        writer.appendln(name);
        String methods = "\"methods\":[";
        writer.appendln(methods);
        writer.indent();
        for (int i = 0; i < seen.size(); i++) {

            // Don't use separator for last entry; will lead to trailing comma in json file
            if (i == seen.size() - 1) {
                separator = "";
            }
            PolyTaintFunction func = seen.get(i);

            String methodEntry = "{\"name\":" + "\"" + func.getFuncSimpleName()
                    + "\",\"parameterTypes\":" + func.getParamTypes() + "}" + separator;
            writer.indents().appendln(methodEntry);

        }
        writer.outdent();
        writer.appendln("]");// close methods array
        writer.appendln("}");// close methods entry
        writer.appendln("]");// close outermost bracket

        writer.writeFile(fileName + StringConstants.JSON_FILE_EXTENSION, outputDir);

    }

    /**
     * Returns a java import statement for the input package
     * 
     * @param pkg
     * @return
     */
    public static String importStatement(String pkg) {
        return "import" + StringConstants.space + pkg + StringConstants.semiColon;
    }

    /**
     * Returns a properly escaped C/Cpp include directive for the input header file.
     * 
     * @param pkg
     * @return
     */
    public static String includeDirective(String header) {

        return "#include " + quoteString(header);
    }

    /**
     * Static initializer with try catch block
     * 
     * @param writer
     * @param code
     */
    public static void addStaticTryCatchBlock(CodeWriter writer, String code) {

        writer.indents().appendln("static {");
        writer.indents().appendln("try {");
        writer.indents().appendln(code + "}");
        writer.indents().appendln("catch (Exception e){} }");

    }

    /**
     * Returns the name of the parent function used to call the input function.
     * 
     * @param func
     * @return
     */
    public static String getParentFuncName(PolyTaintFunction func) {
        return "parent_" + func.getFuncSimpleName();
    }

    /**
     * Surround the string with escaped quotes
     * 
     * @param str
     * @return
     */
    public static String quoteString(String str) {
        return "\"" + str + "\"";
    }

    /**
     * Minifies the input source according to the guest language syntax.
     * 
     * 
     * @param str
     * @return
     */
    public static String minifySource(String src) {
        // escape
        String res = "";

        switch (PolyTaintInstrument.primaryGuest) {
            case JS:
                /**
                 * Minify the source according to JS syntax
                 */
                res = minifyJS(src);
                break;

            case PYTHON:
                /**
                 * Minify the source according to python syntax
                 */
                res = minifyPython(src);

            default:
                break;
        }

        return res;

    }

    /**
     * Escapes all "s in the input string Removes all linebreaks
     * 
     * @param src
     * @return
     */
    public static String minifyJS(String src) {
        return src.replaceAll("\"", "\\\\\"").replace("\n", "").replace("\r", "");
    }



    /**
     * pyuhala: Minifying python src is a bit tricky as we need to take into consideration
     * indentation. We escape all "s, replace all newlines with \n and then all tabs (group of 4
     * spaces) with \t.
     * 
     * @param src
     * @return
     */
    public static String minifyPython(String src) {

        String result = src.replaceAll("\"", "\\\\\"").replace(StringConstants.newLine, "\\n")
                .replace(StringConstants.space_4, "\\t");

        return result;
    }

    /**
     * Print some information for the native image generation
     * 
     * @param filePath
     * @param guestLangaugeId
     * @param outputFolder
     */
    public static void printBuildInfo(String filePath, String guestLangaugeId,
            String outputFolder) {
        String buildInfo = ">>>>>>>> Generating image(s) for: " + filePath + "\n"
                + ">>>>>>>> Output folder: " + outputFolder + "\n";
        System.out.println(buildInfo);
    }

    // For Truffle polyglot API examples
    // https://www.tabnine.com/web/assistant/code/rs/5c662e301095a50001ac417f#L182

}
