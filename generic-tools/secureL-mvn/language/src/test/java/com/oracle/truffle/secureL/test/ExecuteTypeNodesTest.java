/*
 * Created on Wed April 13 2022
 *
 * The MIT License (MIT)
 * Copyright (c) 2022 anonymous-xh anonymous-xh, Institut d'Informatique Université de Neuchâtel (IIUN)
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

package com.oracle.truffle.secureL.test;

import com.oracle.truffle.api.CallTarget;
import com.oracle.truffle.api.Truffle;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import com.oracle.truffle.secureL.nodes.SecureLNode;
import com.oracle.truffle.secureL.nodes.expression.IntLiteralNode;
import com.oracle.truffle.secureL.nodes.SecureLRootNode;

public class ExecuteTypeNodesTest {
    /**
     * Simple test that interprets expressions to return defined types (e.g int,
     * double)
     * and verifies the corresponding values.
     */
    @Test
    public void return_secureL_int() {
        SecureLNode exprNode = new IntLiteralNode(15);
        SecureLRootNode rootNode = new SecureLRootNode(exprNode);
        CallTarget callTarget = rootNode.getCallTarget();// Truffle.getRuntime().createCallTarget(intRootNode);
        Object result = callTarget.call();

        assertEquals(15, result);
    }

    @Test
    public void run_secureL_code() {
        Context context = Context.create();
        // test int
        Value resInt = context.eval("secL", "sInt(25)");
        assertEquals(25, resInt.asInt());

        // test double
        Value resDouble = context.eval("secL", "sDouble(15.5)");
        assertEquals(15.5, resDouble.asDouble(), 0);
        // test bool
        Value boolFalse = context.eval("secL", "sBool(false)");
        assertEquals(0, boolFalse.asInt());

        Value boolTrue = context.eval("secL", "sBool(true)");
        assertEquals(1, boolTrue.asInt());
    }
}
