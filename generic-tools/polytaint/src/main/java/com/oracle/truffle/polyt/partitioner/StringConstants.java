
/*
 * Created on Thursday June 2 2022
 *
 * The MIT License (MIT) Copyright (c) 2022 anonymous-xh anonymous-xh, Institut d'Informatique Université de
 * institution (IIUN)
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

public final class StringConstants {

        /**
         * The following strings represent fully-qualified names of some classes used by the
         * partitioner when creating Java native image programs
         */
        public static final String cEntryPoint = "org.graalvm.nativeimage.c.function.CEntryPoint";
        public static final String pointer = "org.graalvm.word.Pointer";
        public static final String ccharpointer = "org.graalvm.nativeimage.c.type.CCharPointer";
        public static final String nullPointer = "org.graalvm.word.WordFactory.nullPointer();";
        public static final String isoThread = "org.graalvm.nativeimage.IsolateThread";
        public static final String currentIso = "org.graalvm.nativeimage.CurrentIsolate";
        public static final String cFunction = "org.graalvm.nativeimage.c.function.CFunction";

        public static final String javaLangObject = "java.lang.Object";

        public static final String serializer = "org.graalvm.nativeimage.SGXSerializer";
        public static final String sgxSerialize = serializer + ".serialize";
        public static final String sgxDeserialize = serializer + ".deserialize";
        public static final String getCharPointer = serializer + ".getCharPointer";
        public static final String getByteBuffer = serializer + ".getByteBuffer";
        public static final String arrayCopy = serializer + ".arrayCopy";
        public static final String zeroFill = serializer + ".zeroFill";
        // max buffer size for serialized buffers
        /**
         * 256KB: 262144 1MB: 1,048,576
         */
        public static final String maxBufSize = "168041";

        /* JS-Java array object exchange */
        //@formatter:off
        public static final String javaList = "java.util.List";
        public static final String createSimpleList = "public static final TypeLiteral<List<Double>> SIMPLE_LIST = new TypeLiteral<List<Double>>() {};";
        public static final String createDoubleList = "public static final TypeLiteral<List<List<Double>>> DOUBLE_LIST = new TypeLiteral<List<List<Double>>>() {};";
        //@formatter:on


        /* Polyglot api */
        public static final String polyglotx = "org.graalvm.polyglot.*";
        public static final String proxyx = "org.graalvm.polyglot.proxy.*";
        public static final String context = "org.graalvm.polyglot.Context";

        /* File I/O */
        public static final String nioFiles = "java.nio.file.Files";
        public static final String nioPath = "java.nio.file.Path";

        public static final String throwException = "throws Exception";

        /** CEntryPoint options */
        public static final String cEntryPointOptions =
                        "com.oracle.svm.core.c.function.CEntryPointOptions";
        public static final String doNotInclude =
                        "com.oracle.svm.core.c.function.CEntryPointOptions.NotIncludedAutomatically";

        /** Separators */
        public static final String space = " ";
        public static final String space_4 = "    ";
        public static final String temp_tab = "tab";

        public static final String comma = ",";
        public static final String semiColon = ";";
        public static final String newLine = "\n";

        public static final String tab = "\t";

        /** File extensions */
        public static final String JAVA_FILE_EXTENSION = ".java";
        public static final String SGX_EDL_FILE_EXTENSION = ".edl";
        public static final String C_FILE_EXTENSION = ".c";
        public static final String CPP_FILE_EXTENSION = ".cpp";
        public static final String HPP_FILE_EXTENSION = ".hpp";
        public static final String H_FILE_EXTENSION = ".h";
        public static final String JSON_FILE_EXTENSION = ".json";

        /** File names */
        public static final String TRUSTED_IMG = "Trusted.java";
        public static final String UNTRUSTED_IMG = "Untrusted.java";
        public static final String MULTIFUNCTION_CLASS = "MultiFunction.java";

        /** Proxy files */
        public static final String PROXY_IN_CPP = "Proxy_In.cpp";
        public static final String PROXY_IN_H = "Proxy_In.h";
        public static final String PROXY_OUT_CPP = "Proxy_Out.cpp";
        public static final String PROXY_OUT_H = "Proxy_Out.h";

        /** Polytaint EDL file */
        public static final String POLYTAINT_EDL = "Polytaint.edl";

        /** Enclave variables */
        /** Name of global enclave id variable in sgx module */
        public static final String GLOBAL_EID = "global_eid";
        /** Enclave isolate name; ecall threads are attached here by default */
        public static final String ENC_ISO = "global_enc_iso";
        /** App isolate name; ocall threads are attached here by default */
        public static final String APP_ISO = "global_app_iso";
        /** This instruction prints debug information if it is set in the sgx module */
        public static final String DEBUG_INFO = "GRAAL_SGX_INFO();";

        /** MISC */
        public static final String VOID_RET = "void";

        /** Comments */
        public static final String copyrightNotice = "/*\n"
                        + "* This file was generated by PolyTaint code partitioner - ERO project 2022\n"
                        + "*\n" + "* The MIT License (MIT)\n"
                        + "* Copyright (c) 2022 anonymous-xh anonymous-xh\n" + "*\n"
                        + "* Permission is hereby granted, free of charge, to any person obtaining a copy of this software\n"
                        + "* and associated documentation files (the \"Software\"), to deal in the Software without restriction,\n"
                        + "* including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,\n"
                        + "* and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so,\n"
                        + "* subject to the following conditions:\n" + "*\n"
                        + "* The above copyright notice and this permission notice shall be included in all copies or substantial\n"
                        + "* portions of the Software.\n" + "*\n"
                        + "* THE SOFTWARE IS PROVIDED \"AS IS\", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED\n"
                        + "* TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL\n"
                        + "* THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,\n"
                        + "* TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.\n"
                        + "*/\n";

}
