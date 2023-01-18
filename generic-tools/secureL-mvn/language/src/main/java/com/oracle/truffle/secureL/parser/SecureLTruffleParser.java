/*
 * Created on Wed April 14 2022
 *
 * The MIT License (MIT)
 * Copyright (c) 2022 anonymous-xh anonymous-xh, Institut d'Informatique Université de institution (IIUN)
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
package com.oracle.truffle.secureL.parser;

import com.oracle.truffle.secureL.nodes.SecureLNode;
import com.oracle.truffle.secureL.nodes.expression.IntLiteralNode;
import com.oracle.truffle.secureL.nodes.expression.DoubleLiteralNode;
import com.oracle.truffle.secureL.nodes.expression.BoolLiteralNode;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.BailErrorStrategy;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.TerminalNode;

import com.oracle.truffle.secureL.parser.SecureLParser.PrimContext;

import java.io.IOException;
import java.lang.RuntimeException;
import java.util.PrimitiveIterator;
import java.io.Reader;

/**
 * This class transforms SecureL code into a Truffle AST.
 */
public final class SecureLTruffleParser {
    public static SecureLNode parse(String program) {
        CharStream stream = CharStreams.fromString(program);
        return parse(stream);
    }

    public static SecureLNode parse(Reader program) throws IOException {
        CharStream stream = CharStreams.fromReader(program);
        return parse(stream);
    }

    private static SecureLNode parse(CharStream inputStream) {

        SecureLLexer lexer = new SecureLLexer(inputStream);
        SecureLParser parser = new SecureLParser(new CommonTokenStream(lexer));
        // remove the default console error listeners
        lexer.removeErrorListeners();
        parser.removeParseListeners();

        // throw an exception when a parsing error is encountered
        parser.setErrorHandler(new BailErrorStrategy());

        SecureLParser.ExprContext exprContext = parser.start().expr();
        /**
         * anonymous-xh anonymous-xh
         * According to our grammar so far, a secureL expression could either be of
         * subtype
         * "prim" for primitive types or "array" for array types. So we get the
         * resulting
         * prim and array objects from the expression context
         */
        SecureLParser.PrimContext primContext = exprContext.prim();
        SecureLParser.ArrayContext arrayContext = exprContext.array();

        if (primContext != null) {
            // read as: expression to truffle node
            return expr2TruffleRootNode(primContext);
        } else if (arrayContext != null) {
            return expr2TruffleRootNode(arrayContext);
        } else {
            return expr2TruffleRootNode(exprContext);
        }

        // ParserRuleContext context = parser.start().expr();

    }

    /**
     * Return an executable root node for the expression.
     * 
     * @return
     */
    private static SecureLNode expr2TruffleRootNode(ParserRuleContext expr) {

        SecureLNode result = null;// parseIntLiteral("23");

        if (expr instanceof SecureLParser.PrimContext) {
            SecureLParser.PrimContext primCtx = (SecureLParser.PrimContext) expr;
            String type = primCtx.id.getText();
            
            switch (type) {
                case "sInt":
                    result = parseIntLiteral(primCtx.lit.INT().getText());
                    break;

                case "sDouble":
                    result = parseDoubleLiteral(primCtx.lit.DOUBLE().getText());
                    break;

                case "sBool":
                    result = parseBoolLiteral(primCtx.lit.BOOLEAN().getText());
                    break;
                default:
                    throw new RuntimeException("undefined type or not implemented yet: ");

            }

        } else if (expr instanceof SecureLParser.ArrayContext) {
            // TODO
        }

        return result;
    }

    private static SecureLNode parseIntLiteral(String text) {
        try {
            return new IntLiteralNode(Integer.parseInt(text));
        } catch (NumberFormatException e) {
            // it's possible that the integer literal is too big to fit in a 32-bit Java
            // `int` -
            // in that case, fall back to a double literal
            return parseDoubleLiteral(text);
        }
    }

    private static SecureLNode parseBoolLiteral(String text) {
        /**
         * Will default to false
         */
        BoolLiteralNode result = null;

        switch (text) {
            case "true":
            case "1":
                result = new BoolLiteralNode("true");
                break;
            case "false":
            case "0":
                result = new BoolLiteralNode("false");
                break;
            default:
                result = new BoolLiteralNode("false");
                break;

        }
        return result;
    }

    private static SecureLNode parseDoubleLiteral(String text) {
        return new DoubleLiteralNode(Double.parseDouble(text));
    }
}
