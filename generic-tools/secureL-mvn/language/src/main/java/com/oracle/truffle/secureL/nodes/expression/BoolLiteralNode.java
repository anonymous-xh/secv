/*
 * Created on Wed April 13 2022
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
package com.oracle.truffle.secureL.nodes.expression;

import com.oracle.truffle.api.frame.VirtualFrame;

import com.oracle.truffle.secureL.nodes.SecureLNode;

/**
 * AST node representing integer literal expression in secureL.
 */
public final class BoolLiteralNode extends SecureLNode {
    // TODO: make final
    private int value;

    /**
     * 0 for false and 1 for true
     * 
     * @param value
     */
    public BoolLiteralNode(int value) {
        this.value = value;
    }

    /**
     * convert to int if input is string.
     * false for any wrong input
     * 
     * @param value
     */
    public BoolLiteralNode(String value) {
        this.value = 0;
        switch (value) {
            case "true":
                this.value = 1;
                break;
            case "false":
                this.value = 0;
            default:
                break;
        }

    }

    public int executeBool(VirtualFrame frame) {
        return this.value;
    }

    @Override
    public Object executeGeneric(VirtualFrame frame) {
        return this.executeBool(frame);
    }
}
