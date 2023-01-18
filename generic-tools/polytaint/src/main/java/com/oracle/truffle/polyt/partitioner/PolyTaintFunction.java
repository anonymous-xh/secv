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

// truffle
import org.graalvm.polyglot.Source;
import com.oracle.truffle.api.source.SourceSection;
import com.oracle.truffle.api.nodes.Node;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;

// polyt
import com.oracle.truffle.polyt.partitioner.Partitioner.FunctionType;
import com.oracle.truffle.polyt.partitioner.Partitioner.TransitionType;
import com.oracle.truffle.polyt.utils.Logger;
import com.oracle.truffle.polyt.partitioner.StringConstants;

// python
import com.oracle.graal.python.builtins.objects.PNone;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the full structure of a function object used by the polytaint partitioner.
 * This structure comprises: signature: function name, runtime argument types (for dynamically typed
 * languages), return type and the corresponding AST source section
 */
public class PolyTaintFunction {

    private String functionName;
    private List<String> argumentTypes;
    private boolean isMainSymbol;
    private FunctionType functionType;
    private Node astNode;
    private String returnType = StringConstants.VOID_RET;
    private static final String unknownType = "unknown";
    private String sourceBody;

    public PolyTaintFunction(String funcName, FunctionType type, Node node,
            List<String> paramTypes) {
        this.functionName = funcName;
        this.argumentTypes = paramTypes;
        this.functionType = type;
        this.astNode = node;
        this.isMainSymbol = false;
    }

    public void setIsMainSymbol(boolean val) {
        this.isMainSymbol = val;
    }

    public void setASTNode(Node node) {
        this.astNode = node;
    }

    public void setSourceBody(String source) {
        this.sourceBody = source;
    }

    public void setParamTypes(List<String> paramTypes) {
        // Logger.log("setting param types for : " + this.functionName);
        this.argumentTypes = new ArrayList<String>(paramTypes);
        // Logger.log("after set size: " + this.argumentTypes.size());
        // for (int i = 0; i < this.argumentTypes.size(); i++) {
        // Logger.log("Type " + i + " = " + this.argumentTypes.get(i));
        // }
    }

    public String getSourceBody() {
        return this.sourceBody;
    }

    public boolean getIsMainSymbol() {
        return this.isMainSymbol;
    }

    public List<String> getArgumentTypes() {
        return this.argumentTypes;
    }

    public void setFunctionType(FunctionType type) {
        this.functionType = type;
    }

    public FunctionType getFunctionType() {
        return this.functionType;
    }

    public String getReturnType() {
        return this.returnType;
    }

    public void setReturnType(String type) {
        this.returnType = type;
    }

    public String getFunctionName() {
        return this.functionName;
    }

    public String getProxyReturnType() {
        /**
         * Proxy functions are native methods. Will return CCharPointer representing the byte array
         * of the serialized object.
         */
        String type = this.returnType;
        if (type.equals("Object")) {
            type = "CCharPointer";

        }
        return type;
    }

    /*
     * Returns the simple name corresponding to the function. The logic in here follows the naming
     * conventions I use in the polytaint code. That is, every function in a file has fullname:
     * filename.ext.funcName and the corresponding simplename is: funcName
     */
    public String getFuncSimpleName() {

        String fullName = this.getFunctionName();
        String simpleName = fullName.substring(fullName.lastIndexOf('.') + 1);
        return simpleName;
    }

    public Node getAstNode() {
        return this.astNode;
    }

    public int getArgCount() {
        return this.argumentTypes.size();
    }

    /**
     * prints the different param types taken by this function at runtime. This signature could
     * change depending on different runtime inputs.
     */
    public String getSignature() {
        String signature = "(";
        String sep = ",";
        String type;

        for (int i = 0; i < this.argumentTypes.size(); i++) {
            if (i == this.argumentTypes.size() - 1) {
                sep = "";
            }
            type = this.argumentTypes.get(i);
            signature += type + sep;
        }

        signature += ")";
        return signature;
    }

    /**
     * Get the corresponding code for the method definition
     * 
     * @return
     */
    public String getMethodDefition() {
        String source = astNode.getSourceSection().getCharacters().toString();
        return source;
    }

    /**
     * Get the runtime type of a specific argument. This information will be used when creating the
     * native image entry point in the partitioning sub-module.
     */
    public static String getType(Object var) {
        String type = unknownType;

        // Logger.log("getType for var: " + var.toString());

        // else if (var instanceof Long) {
        // type = "long";}

        if ((var instanceof Integer)) {
            type = "int";
        } else if (var instanceof Float) {
            type = "float";
        } else if (var instanceof Double) {
            type = "double";
        } else if (var instanceof Boolean) {
            type = "boolean";
        } else if (var instanceof Short) {
            type = "short";
        }
        // for python
        else if (var instanceof PNone) {
            type = "";
        }
        if (type.equals(unknownType)) {
            /**
             * PYuhala: use a generic object type here
             */
            // System.out.println("unknown type for:" + var + " getClass: " + var.getClass());
            type = "Object";

        }
        return type;
    }

    /**
     * A Truffle polyglot context returns a Value object. This should be converted to the
     * appropriate type if need be.
     * 
     * @return
     */
    public String getContextRet() {
        String ret = "";
        String retType = this.returnType;

        if (retType.equals("bool")) {
            ret = "asBoolean()";
        } else if (retType.equals("byte")) {
            ret = "asByte()";
        } else if (retType.equals("short")) {
            ret = "asShort()";
        } else if (retType.equals("float")) {
            ret = "asFloat()";
        } else if (retType.equals("int")) {
            ret = "asInt()";
        } else if (retType.equals("long")) {
            ret = "asLong()";
        } else if (retType.equals("double")) {
            ret = "asDouble()";
        } else if (retType.equals("Object")) {
            ret = "";
        }

        else {
            // return long if all tests fail
            ret = "as";
        }
        return ret;
    }

    /**
     * Get the appropriate type for a returned value.
     * 
     * @param result
     * @return
     */
    public static String getReturnType(Object result) {

        if (result == null) {
            return "void";
        }
        String res = result.toString();

        String retType = "unknown";
        if (res.contains("<undefined>") || res.contains("None") || res == "") {
            // for JS
            retType = "void";
            // TODO: for ruby
        } else {
            retType = getType(result);
        }
        // Logger.log("getReturnType: result class = " + retType);
        return retType;
    }

    /**
     * Returns comma separated list of parameter types For example: ["int","boolean"]
     * 
     * @return
     */
    public String getParamTypes() {
        String typeList = "[";
        String sep = ",";
        List<String> argTypes = this.argumentTypes;
        int size = argTypes.size();

        for (int i = 0; i < argumentTypes.size(); i++) {
            String type = argTypes.get(i);
            if (i == size - 1) {
                sep = "";
            }
            if (type.equals("Object")) {
                typeList += "\"" + "java.lang.Object" + "\"" + sep;
            } else {
                typeList += "\"" + type + "\"" + sep;
            }

        }
        typeList += "]";
        return typeList;
    }

    /**
     * Returns a string with the param definitions
     * 
     * @param isGraalEntryPoint
     * @return
     */
    public String getParamSignature(boolean isGraalEntryPoint) {
        String paramSignature = isGraalEntryPoint ? "(IsolateThread thread" : "(";

        List<String> argTypes = this.argumentTypes;
        int size = argTypes.size();

        if (size == 0) {
            paramSignature += ")";
        } else {

            for (int i = 0; i < argumentTypes.size(); i++) {
                String type = argTypes.get(i);
                if (i == 0 && !isGraalEntryPoint) {
                    paramSignature += argumentTypes.get(i) + " param" + (i + 1);
                } else {
                    paramSignature += ", " + argumentTypes.get(i) + " param" + (i + 1);
                }
            }
            // close parentheses
            paramSignature += ")";
        }

        return paramSignature;
    }

    /**
     * Return signature for transition ecall or ocall transition routine.
     * 
     * 
     * @return
     */
    public String getTransitionSignature() {
        String paramSignature = "(";

        List<String> argTypes = this.argumentTypes;
        int size = argTypes.size();

        if (size == 0) {
            paramSignature += ")";
        } else {

            for (int i = 0; i < argumentTypes.size(); i++) {
                String type = argTypes.get(i);
                if (i == 0) {
                    if (type.equals("Object")) {
                        // add length param = size of serialized byte array.
                        paramSignature += "void* param" + (i + 1) + ",int len" + (i + 1);
                    } else {
                        paramSignature += argumentTypes.get(i) + " param" + (i + 1);
                    }

                } else {
                    if (type.equals("Object")) {
                        // add length param = size of serialized byte array.
                        paramSignature += ",void* param" + (i + 1) + ",int len" + (i + 1);
                    } else {
                        paramSignature += ", " + argumentTypes.get(i) + " param" + (i + 1);
                    }


                }
            }

            /**
             * For object return add pointer which will contain all serialized data.
             */
            if (this.hasObjectReturn()) {
                // test value with 4kb = 4 * 1024 bytes.
                paramSignature += ",void* retPtr,int retLen";
            }

            // close parentheses
            paramSignature += ")";
        }

        return paramSignature;
    }

    /**
     * Returns signature for C routines. Object types are changed to void *
     * 
     * 
     * @return
     */
    public String getCParamSignature() {
        String paramSignature = "(";

        List<String> argTypes = this.argumentTypes;
        int size = argTypes.size();

        if (size == 0) {
            // TODO: handle object return types without param else silent bug
            paramSignature += ")";
        } else {

            for (int i = 0; i < argumentTypes.size(); i++) {
                String type = argTypes.get(i);
                if (i == 0) {
                    if (type.equals("Object")) {
                        paramSignature += "void* param" + (i + 1) + ", int len" + (i + 1);
                    } else {
                        paramSignature += type + " param" + (i + 1);
                    }

                } else {
                    if (type.equals("Object")) {
                        paramSignature += ", void* param" + (i + 1) + ", int len" + (i + 1);
                    } else {
                        paramSignature += ", " + type + " param" + (i + 1);
                    }

                }
            }

            /**
             * For object return add pointer which will contain all serialized data.
             */
            if (this.hasObjectReturn()) {
                // test value with 4kb = 4 * 1024 bytes.
                paramSignature += ",void* retPtr,int retLen";
            }

            // close parentheses
            paramSignature += ")";
        }

        return paramSignature;
    }

    /**
     * Returns the signature to be used in EDL files. Here we need to take extra care with
     * void/object params.
     * 
     * @return
     */
    public String getEdlSignature() {
        String paramSignature = "(";

        List<String> argTypes = this.argumentTypes;
        int size = argTypes.size();

        if (size == 0) {
            // TODO: handle object return types without param else silent bug
            paramSignature += ")";
        } else {

            for (int i = 0; i < argumentTypes.size(); i++) {
                String type = argTypes.get(i);
                String param = "param" + (i + 1);
                String len = "len" + (i + 1);
                if (i == 0) {
                    if (type.equals("Object")) {
                        paramSignature += "[in,size=" + len + ",count=1]void* param" + (i + 1)
                                + ", int " + len;
                    } else {
                        paramSignature += type + " param" + (i + 1);
                    }

                } else {
                    if (type.equals("Object")) {
                        // paramSignature += ", [in,size=1,count=268435456]void* param" + (i + 1);
                        paramSignature += ",[in,size=1,count=" + len + "]void* param" + (i + 1)
                                + ", int " + len;
                    } else {
                        paramSignature += ", " + type + " param" + (i + 1);
                    }

                }
            }

            /**
             * For object return add pointer which will contain all serialized data.
             */
            if (this.hasObjectReturn()) {
                // test value with 4kb = 4 * 1024 bytes.
                paramSignature += ",[out,size=retLen]void* retPtr,int retLen";
            }

            // close parentheses
            paramSignature += ")";
        }

        return paramSignature;
    }



    /**
     * Returns a string with the param definitions
     * 
     * @param isGraalEntryPoint
     * @return
     */
    public String getEntryPointSignature() {
        String paramSignature = "(IsolateThread thread";
        List<String> argTypes = this.argumentTypes;
        int size = argTypes.size();

        if (size == 0) {
            // TODO: handle object return types without param else silent bug
            paramSignature += ")";
        } else {

            for (int i = 0; i < size; i++) {
                String type = argTypes.get(i);
                /**
                 * Object parameters have to be deserialized. The CCharPointer is the first address
                 * of the byte array to be deserialized and len the array's size;
                 */
                if (type.equals("Object")) {
                    type = "CCharPointer";
                    paramSignature += ", " + type + " param" + (i + 1) + ",int len" + (i + 1);
                } else {
                    paramSignature += ", " + type + " param" + (i + 1);
                }


            }

            /**
             * For object return add pointer which will contain all serialized data.
             */
            // if (this.hasObjectReturn()) {
            // // test value with 4kb = 4 * 1024 bytes.
            // paramSignature += ",CCharPointer retPtr,int retLen";
            // }
            // close parentheses
            paramSignature += ")";
        }

        return paramSignature;
    }

    /**
     * Returns a string with the param definitions
     * 
     * @param isGraalEntryPoint
     * @return
     */
    public String getProxySignature() {
        String paramSignature = "(";
        List<String> argTypes = this.argumentTypes;
        int size = argTypes.size();

        if (size == 0) {
            paramSignature += ")";
        } else {

            for (int i = 0; i < size; i++) {
                String type = argTypes.get(i);
                String val = "";
                /**
                 * object types have been serialized into a byte array represented by a ccharpointer
                 */


                if (i == 0) {
                    if (type.equals("Object")) {
                        val = "CCharPointer param" + (i + 1) + ",int len" + (i + 1);
                    } else {
                        val = type + " param" + (i + 1);
                    }
                    paramSignature += val;
                } else {
                    if (type.equals("Object")) {
                        val = "," + "CCharPointer param" + (i + 1) + ",int len" + (i + 1);
                    } else {
                        val = ", " + type + " param" + (i + 1);
                    }
                    paramSignature += val;
                }
            }

            /**
             * For object return types, we will add an extra parameter which represents the buffer
             * to receive the serialized buffer. This will be passed in the ecall transition with
             * the [out,retLen] strategy.
             */
            if (this.hasObjectReturn()) {
                paramSignature += ",CCharPointer retPtr, int retLen";
            }
            // close parentheses
            paramSignature += ")";
        }

        return paramSignature;
    }

    /**
     * Returns the param class name for the function: Example: file.js.funcA has param class name:
     * Param_funcA
     * 
     * @param func
     * @return
     */
    public String getParamClassName() {
        return "Param_" + this.getFuncSimpleName();
    }

    /**
     * Returns a string corresponding to a call of this function with its params. For example:
     * (param1,param2)
     * 
     * @return
     */
    public String getCallInvocation() {
        String callInvoation = "(";

        List<String> argTypes = this.argumentTypes;
        if (argTypes.size() == 0) {
            callInvoation += ")";
        } else {

            for (int i = 0; i < argTypes.size(); i++) {
                String type = argTypes.get(i);
                if (i == 0) {

                    if (type.equals("Object")) {
                        callInvoation += "param_val" + (i + 1);
                    } else {
                        callInvoation += "param" + (i + 1);
                    }

                } else {
                    if (type.equals("Object")) {
                        callInvoation += ", " + "param_val" + (i + 1);
                    } else {
                        callInvoation += ", " + "param" + (i + 1);
                    }


                }
            }
            // close parentheses
            callInvoation += ")";
        }

        return callInvoation;
    }

    /**
     * Call invocation in entry point method. This is similar
     * 
     * @return
     */
    public String getEntryCallInvocation() {
        String callInvoation = "(";

        List<String> argTypes = this.argumentTypes;
        if (argTypes.size() == 0) {
            callInvoation += ")";
        } else {

            for (int i = 0; i < argTypes.size(); i++) {
                String type = argTypes.get(i);
                if (i == 0) {

                    if (type.equals("Object")) {
                        // it result of deserialization
                        callInvoation += "obj_param" + (i + 1);
                    } else {
                        callInvoation += "param" + (i + 1);
                    }

                } else {
                    if (type.equals("Object")) {
                        callInvoation += ", " + "obj_param" + (i + 1);
                    } else {
                        callInvoation += ", " + "param" + (i + 1);
                    }


                }
            }
            // close parentheses
            callInvoation += ")";
        }

        return callInvoation;
    }


    /**
     * Returns the corresponding proxy call invocation. This is similar to getCallInvocation but
     * Object parameters are replaced with their serialized versions.
     * 
     * @return
     */
    public String getProxyCallInvocation() {
        String callInvoation = "(";

        List<String> argTypes = this.argumentTypes;
        if (argTypes.size() == 0) {
            callInvoation += ")";
        } else {

            for (int i = 0; i < argTypes.size(); i++) {
                String type = argTypes.get(i);
                String param = "param" + (i + 1);
                if (i == 0) {

                    if (type.equals("Object")) {

                        callInvoation += "ptr_" + param + ",len_" + (i + 1);
                    } else {
                        callInvoation += "param" + (i + 1);
                    }

                } else {
                    if (type.equals("Object")) {
                        callInvoation += ", " + "ptr_" + param + ", len_" + (i + 1);

                    } else {
                        callInvoation += ", " + "param" + (i + 1);
                    }


                }
            }

            /**
             * For object return add pointer which will contain all serialized data.
             */
            if (this.hasObjectReturn()) {
                // test value with 4kb = 4 * 1024 bytes.
                callInvoation += ",retPtr," + StringConstants.maxBufSize;
            }

            // close parentheses
            callInvoation += ")";
        }

        return callInvoation;
    }

    // String retBuf = "byte[] retBuf = new byte[" + BUFSIZ + "];";
    // String retPtr = ccharpointer + space + "retPtr = " + getCharPointer + "(retBuf);";


    /**
     * Returns code to serialize all object parameters NB: the same naming convention
     * 
     * @return
     */
    public void addSerializedParams(CodeWriter writer) {

        List<String> argTypes = this.getArgumentTypes();
        for (int i = 0; i < argTypes.size(); i++) {
            String type = argTypes.get(i);
            String param = "param" + (i + 1);
            if (type.equals("Object")) {
                //@formatter:off
                String byteArray =
                        "byte[] bytes_" + param + " = SGXSerializer.serialize(" + param + ");";
                String len = "int len_" + (i+1) + " = bytes_" + param + ".length;";                
                //String temp_buf = "byte[] tempBuf = new byte[256 * 1024 * 1024];";                
                //String array_copy = "SGXSerializer.arrayCopy(tempBuf, bytes_" + param + ", len_" + param+");";
                String ret_ptr = "CCharPointer ptr_" + param+ " = SGXSerializer.getCharPointer(bytes_"+param+");";
                // @formater:on
                writer.indents().appendln(byteArray);
                writer.indents().appendln(len);
                //writer.indents().appendln(temp_buf);
                writer.indents().appendln(ret_ptr);
                //writer.indents().appendln(array_copy);

            }
        }


    }

    public void deserializeParams(CodeWriter writer){
        List<String> argTypes = this.getArgumentTypes();
        for (int i = 0; i < argTypes.size(); i++) {
            String type = argTypes.get(i);
            String param = "param" + (i + 1);
            if (type.equals("Object")) {
                //@formatter:off
                String getBuffer = "byte[] bytes_"+(i+1)+" = SGXSerializer.getByteBuffer(" + param + ", len"+(i+1)+");";
                String getObject = "Object obj_" + param + " = SGXSerializer.deserialize(bytes_" + (i + 1) + ");";                
                // @formater:on
                writer.indents().appendln(getBuffer);
                writer.indents().appendln(getObject);
               
            }
        }
    }

    /**
     * Get code to serialize return value
     * This is done in a CEntryPoint method in the side returning the array/object to be serialized.
     * @param writer
     */
    public void serializeReturnValue(CodeWriter writer,String inv){        
       // String getByteBuffer = "byte[] retBuf = SGXSerializer.getByteBuffer(retPtr,retLen);";
        String getObj = "Object retObj = " + inv;
        String getVal = "Value val = Value.asValue(retObj);";
        String getListS = "List<Double> listS = val.as(SIMPLE_LIST);";
        
        String getListD = "List<List<Double>> listD = val.as(DOUBLE_LIST);";
        String getSimpleArray = "Double[] simpleArray = listS.toArray(new Double[0]);";

        String getDoubleArray = "Double [][] doubleArray = listD.stream().map(l -> l.toArray(new Double[l.size()])).toArray(Double[][]::new);";
   
        String serializeSimpleArray = "byte[] bytes = SGXSerializer.serialize(simpleArray);";
        String serializeDoubleArray = "byte[] bytes = SGXSerializer.serialize(doubleArray);";
        String getLen = "int len = bytes.length;";                
        //String getTmpRetBuf = "byte[] tempRetBuf = new byte[4096];";                
        //String copyBuf = "SGXSerializer.arrayCopy(retBuf, bytes, len);";
        //String getRetPtr = "CCharPointer retPtr = SGXSerializer.getCharPointer(tempRetBuf);";
        //String returnStatement = "return retPtr;";

        String getBytePtr = "CCharPointer bytePtr = SGXSerializer.getCharPointer(bytes);";
        String returnStatement = "return bytePtr;";

        //writer.indents().appendln(getByteBuffer);
        writer.indents().appendln(getObj);
        writer.indents().appendln(getVal);
        //writer.indents().appendln(getListD);
        writer.indents().appendln(getListD);

        writer.indents().appendln(getDoubleArray);
        //writer.indents().appendln(serializeSimpleArray);
        writer.indents().appendln(serializeDoubleArray);
        writer.indents().appendln(getLen);
        writer.indents().appendln(getBytePtr);
        writer.indents().appendln(returnStatement);
        //writer.indents().appendln(getTmpRetBuf);
        //writer.indents().appendln(copyBuf);
        //writer.indents().appendln(getRetPtr);
        //writer.indents().appendln(returnStatement);


    }


    /**
     * Gets ccharpointer from proxy, obtains corresponding bytes, and deserializes the byte array into original object.
     * Object is then boxed as a Truffle Value and returned.
     * @param writer
     * @param proxyInv
     */
    public void returnObjFromProxy(CodeWriter writer, String proxyInv){
   
        //@formatter:off
        String allocateReturnBuf = "byte[] tempRetBuf = new byte["+StringConstants.maxBufSize+"];"; //this will contain the serialized return value
        String getRetPtr = "CCharPointer retPtr = SGXSerializer.getCharPointer(tempRetBuf);"; //pass this to enclave transition/proxy            
        //String getProxyPtr = "CCharPointer proxyPtr = " + proxyInv;
        String doInvocation  = proxyInv;
        //String getProxyBytes = "byte[] proxyBytes = SGXSerializer.getByteBuffer(proxyPtr, 256 * 1024 * 1024);";
        String getObject = "Object objFromProxy = SGXSerializer.deserialize(tempRetBuf);"; 
        String castToSimpleArray = "Double[] simpleArray = (Double[]) objFromProxy;";      
        
        String castToDoubleArray = "Double[][] doubleArray = (Double[][]) objFromProxy;"; 
        String returnStatement = "return Value.asValue(doubleArray);";             
        //@formatter:on
        writer.indents().appendln(allocateReturnBuf);
        writer.indents().appendln(getRetPtr);
        writer.indents().appendln(doInvocation);

        // writer.indents().appendln(getProxyPtr);
        // writer.indents().appendln(getProxyBytes);
        writer.indents().appendln(getObject);
        // writer.indents().appendln(castToSimpleArray);
        writer.indents().appendln(castToDoubleArray);
        writer.indents().appendln(returnStatement);
    }

    /**
     * Test if a function has object parameters
     * 
     * @return
     */
    public boolean hasObjectParam() {
        boolean test = false;

        for (String type : this.argumentTypes) {
            if (type.equals("Object")) {
                test = true;
                break;
            }
        }

        return test;
    }

    /**
     * Test if a function has object parameters
     * 
     * @return
     */
    public boolean hasObjectReturn() {
        return this.getReturnType().equals("Object");
    }

    /**
     * 
     * @param writer
     */
    public void addProxyReturn(CodeWriter writer) {

    }


    /**
     * Returns a string corresponding to a call of this function with its params. For example:
     * (param1,param2)
     * 
     * @return
     */
    public String getEntryInvocation(String isolate) {
        String callInvocation = "(" + isolate;

        List<String> argTypes = this.getArgumentTypes();
        int size = argTypes.size();

        if (size == 0) {
            callInvocation += ")";
        } else {

            for (int i = 0; i < size; i++) {
                String type = argTypes.get(i);
                if (type.equals("Object")) {
                    callInvocation += ", " + "(char*)param" + (i + 1) + ",len" + (i + 1);
                } else {
                    callInvocation += ", " + "param" + (i + 1);
                }


            }
            /**
             * Objects will be returned via an additional pointer parameter.
             */
            // if(this.hasObjectReturn()){
            // callInvocation += ",(char*)retPtr,retLen";
            // }
            // close parentheses
            callInvocation += ")";
        }

        return callInvocation;
    }


    public String getTransInvocation(boolean isEcall) {
        if (isEcall) {
            return getEcallInvocation();
        } else {
            return getOcallInvocation();
        }
    }

    /**
     * Returns a string corresponding to a call invocation of the corresponding ecall/ocall
     * transition. For example: ocall_funcA(&ret,param1,param2) or ecall_funcB(global_eid, param)
     * 
     * @return
     */
    public String getEcallInvocation() {
        String callInvocation = "(global_eid";
        if (!returnType.equals("void") && !this.hasObjectReturn()) {

            callInvocation += ",&ret";

        }

        if (this.argumentTypes.size() == 0) {
            // TODO: handle funcs with object returns but no arguments.
            callInvocation += ")";
        } else {

            for (int i = 0; i < argumentTypes.size(); i++) {
                if (i == 0) {

                    /**
                     * we already have the first param (global_eid) or ret type; so add a comma
                     */
                    callInvocation += ", " + "param" + (i + 1);

                } else {
                    callInvocation += ", " + "param" + (i + 1);
                }
            }
            /**
             * For object types add additional pointer param to contain serialized buffer.
             */
            if (this.hasObjectReturn()) {

                callInvocation += ",retPtr,retLen";
            }
            callInvocation += ")";
        }

        return callInvocation;
    }

    /**
     * Gets ocall invocation for function
     * 
     * @return
     */
    public String getOcallInvocation() {
        String callInvocation = "(";
        String sep = "";
        if (!returnType.equals("void") && !this.hasObjectReturn()) {
            callInvocation += "&ret";
            sep = ",";
        }

        List<String> argTypes = this.getArgumentTypes();
        int size = argTypes.size();
        if (size == 0) {
            callInvocation += ")";
        } else {
            callInvocation += sep;
            for (int i = 0; i < size; i++) {
                String type = argTypes.get(i);
                if (i == 0) {
                    if (type.equals("Object")) {
                        callInvocation += "param" + (i + 1) + ", len" + (i + 1);
                    } else {
                        callInvocation += "param" + (i + 1);
                    }

                } else {
                    sep = ",";
                    if (type.equals("Object")) {
                        callInvocation += sep + " param" + (i + 1) + ", len" + (i + 1);
                    } else {
                        callInvocation += sep + " param" + (i + 1);
                    }


                }
            }
            /**
             * For object types add additional pointer param to contain serialized buffer.
             */
            if (this.hasObjectReturn()) {

                callInvocation += ",retPtr,retLen";
            }
            // close parentheses
            callInvocation += ")";
        }

        return callInvocation;
    }

}
