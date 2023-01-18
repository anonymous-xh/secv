/*
 * Created on Wed April 14 2022
 *
 * The MIT License (MIT)
 * Copyright (c) 2022 Peterson Yuhala, Institut d'Informatique Université de Neuchâtel (IIUN)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software
 * and associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
 * TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
 * THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.oracle.truffle.secureL;

import com.oracle.truffle.secureL.nodes.SecureLNode;
import com.oracle.truffle.secureL.nodes.SecureLRootNode;
import com.oracle.truffle.secureL.parser.SecureLTruffleParser;

import com.oracle.truffle.api.CallTarget;
import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.TruffleLanguage;

import org.antlr.v4.runtime.misc.ParseCancellationException;

/**
 * This class registers SecureL as a Truffle language which can be used
 * as part of the GraalVM polyglot API. 
 */

@TruffleLanguage.Registration(id = "secL", name = "Secure Language")
public final class SecureLLanguage extends TruffleLanguage<Void>{
    /**
     * This is the callback method used by the GraalVM polyglot API 
     * when the secureL language ID is evaluated in 
     * {@link org.graalvm.polyglot.Context#eval(String, CharSequence) evaluated}.
     */

     @Override
     protected CallTarget parse(ParsingRequest request) throws Exception{
         SecureLNode exprNode = SecureLTruffleParser.parse(request.getSource().getReader());
         SecureLRootNode rootNode = new SecureLRootNode(exprNode);
         //return Truffle.getRuntime().createCallTarget(rootNode);
         return rootNode.getCallTarget();
     }

     /**
     * This is an abstract method in {@link TruffleLanguage},
     * so you have to override it,
     * We won't be needing a Context for secureL, just type definitions is enough, 
     * atleast for now.
     * so we just return {@code null} here.
     */
    @Override
    protected Void createContext(Env env) {
        return null;
    }
}
