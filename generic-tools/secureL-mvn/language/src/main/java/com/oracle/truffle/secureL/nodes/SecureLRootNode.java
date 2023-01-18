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
package com.oracle.truffle.secureL.nodes;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.RootNode;

/**
 * A Truffle AST must be anchored in a {@link RootNode} to be executed.
 * Since {@link RootNode} is an abstract class, we need to subclass it, and
 * override the abstract {@link #execute} method.
 */
public class SecureLRootNode extends RootNode {
    @SuppressWarnings("FieldMayBeFinal")
    @Child
    private SecureLNode exprNode;

    public SecureLRootNode(SecureLNode exprNode) {
        super(null);
        this.exprNode = exprNode;
    }

    /**
     * The execute method for this AST.
     * It simply delegates to the {@link SecureLNode#executeGeneric} method
     * of the instance that was passed to it in the constructor.
     */
    @Override
    public Object execute(VirtualFrame frame) {

        return this.exprNode.executeGeneric(frame);
    }
}
