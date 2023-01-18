/*
 * Created on Wed Mar 31 2022
 *
 * The MIT License (MIT) Copyright (c) 2022 anonymous-xh anonymous-xh
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

package com.oracle.truffle.polyt.nodes;

// truffle-api
import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.instrumentation.EventContext;

// polyt
import com.oracle.truffle.polyt.SourceFragmentConsts.SourceInfoID;
import com.oracle.truffle.polyt.PolyTaintInstrument;
import com.oracle.truffle.polyt.partitioner.PolyTaintFunction;
import com.oracle.truffle.polyt.partitioner.Partitioner.FunctionType;
import com.oracle.truffle.polyt.TaintTracker;
import com.oracle.truffle.polyt.utils.Logger;

public class VarReadEventNode extends PolyTaintNode {

    public VarReadEventNode(PolyTaintInstrument instrument, EventContext context) {
        super(instrument, context);

        this.name = getPolyTaintNodeName(PolyTaintInstrument.guestLanguageId);

    }

    @Override
    public void onInputValue(VirtualFrame frame, EventContext inputContext, int inputIndex,
            Object inputValue) {
        System.out.println(">>>>> on input value event occurred >>");
    }


    @Override
    public void onReturnValue(VirtualFrame vFrame, Object result) {

    }
}
