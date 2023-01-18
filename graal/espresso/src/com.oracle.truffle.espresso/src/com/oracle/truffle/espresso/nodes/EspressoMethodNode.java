/*
 * Copyright (c) 2019, 2021, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */
package com.oracle.truffle.espresso.nodes;

import java.util.List;

import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.oracle.truffle.api.instrumentation.StandardTags;
import com.oracle.truffle.api.instrumentation.Tag;
import com.oracle.truffle.api.source.Source;
import com.oracle.truffle.api.source.SourceSection;
import com.oracle.truffle.espresso.classfile.attributes.LineNumberTableAttribute;
import com.oracle.truffle.espresso.impl.Method;
import com.oracle.truffle.espresso.impl.Method.MethodVersion;
import com.oracle.truffle.espresso.impl.ObjectKlass;
import com.oracle.truffle.espresso.meta.EspressoError;

/**
 * Base node for all implementations of Java methods.
 */
public abstract class EspressoMethodNode extends EspressoBaseMethodNode {

    private final MethodVersion method;
    private SourceSection sourceSection;

    EspressoMethodNode(MethodVersion method) {
        this.method = method;
    }

    @Override
    public MethodVersion getMethodVersion() {
        return method;
    }

    @Override
    public final Method getMethod() {
        return method.getMethod();
    }

    public final ObjectKlass getDeclaringKlass() {
        return getMethod().getDeclaringKlass();
    }

    @Override
    public boolean hasTag(Class<? extends Tag> tag) {
        return tag == StandardTags.RootTag.class;
    }

    @TruffleBoundary
    @Override
    public final SourceSection getSourceSection() {
        Source s = getSource();
        if (s == null) {
            return null;
        }

        if (sourceSection == null) {
            SourceSection localSourceSection = null;
            LineNumberTableAttribute lineNumberTable = method.getLineNumberTableAttribute();

            if (lineNumberTable != LineNumberTableAttribute.EMPTY) {
                List<LineNumberTableAttribute.Entry> entries = lineNumberTable.getEntries();
                int startLine = Integer.MAX_VALUE;
                int endLine = 0;

                for (int i = 0; i < entries.size(); i++) {
                    int line = entries.get(i).getLineNumber();
                    if (line > endLine) {
                        endLine = line;
                    }
                    if (line < startLine) {
                        startLine = line;
                    }
                }

                if (startLine >= 1 && endLine >= 1 && startLine <= endLine) {
                    localSourceSection = s.createSection(startLine, 1, endLine, 1);
                } // (else) Most likely generated bytecodes with dummy LineNumberTable attribute.
            }
            if (localSourceSection == null) {
                localSourceSection = s.createUnavailableSection();
            }
            synchronized (this) {
                if (sourceSection == null) {
                    sourceSection = localSourceSection;
                }
            }
        }
        return sourceSection;
    }

    public final Source getSource() {
        return getMethod().getSource();
    }

    @Override
    public boolean shouldSplit() {
        return false;
    }

    public EspressoMethodNode split() {
        throw EspressoError.shouldNotReachHere();
    }
}
