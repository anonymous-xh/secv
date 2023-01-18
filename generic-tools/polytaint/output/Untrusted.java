/*
* This file was generated by PolyTaint code partitioner - ERO project 2022
*
* The MIT License (MIT)
* Copyright (c) 2022 Peterson Yuhala
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


package polytaint;
import org.graalvm.nativeimage.CurrentIsolate;
import org.graalvm.nativeimage.IsolateThread;
import org.graalvm.nativeimage.c.function.CEntryPoint;
import org.graalvm.nativeimage.c.function.CFunction;
import org.graalvm.nativeimage.c.type.CCharPointer;
import org.graalvm.polyglot.*;
import org.graalvm.polyglot.proxy.*;
import org.graalvm.polyglot.*;
import org.graalvm.nativeimage.SGXSerializer;
import java.util.List;
import org.graalvm.polyglot.*;

public class Untrusted {

    public static final TypeLiteral<List<Double>> SIMPLE_LIST = new TypeLiteral<List<Double>>() {};
    public static final TypeLiteral<List<List<Double>>> DOUBLE_LIST = new TypeLiteral<List<List<Double>>>() {};
    public static void main(String[] args) {
        System.out.println("Main: untrusted partition!!");
        Context context = Context.newBuilder().allowAllAccess(true).build();
        Value doPageRank = context.asValue(Untrusted.class).getMember("static").getMember("doPageRank");
        Value generateAndPreprocessGraph = context.asValue(Untrusted.class).getMember("static").getMember("generateAndPreprocessGraph");
        context.eval("js","function main_wrapper(doPageRank,generateAndPreprocessGraph){doPageRank(4000);}main_wrapper;").execute(doPageRank,generateAndPreprocessGraph);
    }
    public static void doPageRank(int param1){
        doPageRank_proxy(param1);
    }

    public static Object generateAndPreprocessGraph(int param1){
        Context context = Context.newBuilder().allowAllAccess(true).build();
        Value doPageRank = context.asValue(Untrusted.class).getMember("static").getMember("doPageRank");
        return context.eval("js","function generateAndPreprocessGraph_wrapper(doPageRank,param1){function generateAndPreprocessGraph(n) {    console.log(\">>>>>>> generating directed RMAT graph with : \", n, \" nodes and \", n * 2, \" edges >>>>>>>\");    var edgesToGenerate = n * 2;    var gen_start = new Date();    var graph = new Array(n);    for (var i = 0; i < n; i++) {        graph[i] = new Array(n);        graph[i].fill(0);    }    var pA = 0.45; var pB = 0.15; var pC = 0.15; var pD = 0.25;    var createdEdges = 0;    var nEdgesATime = edgesToGenerate;    var cumA = pA; var cumB = cumA + pB; var cumC = cumB + pC; var cumD = 1.0;    var fromIds = new Array(ne);    var toIds = new Array(ne);    while (edgesToGenerate > createdEdges) {        var ne = Math.min((edgesToGenerate - createdEdges), nEdgesATime);        for (var j = 0; j < ne; j++) {            var col_st = 0; var col_en = n - 1; var row_st = 0; var row_en = n - 1;            while (col_st != col_en || row_st != row_en) {                var x = Math.random();                if (x < cumA) {                    col_en = col_st + (col_en - col_st) / 2;                    row_en = row_st + (row_en - row_st) / 2;                } else if (x < cumB) {                    col_st = col_en - (col_en - col_st) / 2;                    row_en = row_st + (row_en - row_st) / 2;                } else if (x < cumC) {                    col_en = col_st + (col_en - col_st) / 2;                    row_st = row_en - (row_en - row_st) / 2;                } else {                    col_st = col_en - (col_en - col_st) / 2;                    row_st = row_en - (row_en - row_st) / 2;                }            }            fromIds[j] = Math.floor(col_st);            toIds[j] = Math.floor(row_st);        }        createdEdges += ne;    }    console.log(\"----> graph generation time = \", new Date() - gen_start, \" ms\");    var adj_start = new Date();    console.log(\"--- converting graph to adjacency matrix ---\");    for (var i = 0; i < edgesToGenerate; i++) {        var x = fromIds[i];        var y = toIds[i];        graph[x][y] = 1;    }    console.log(\"--- converting adjacency matrix to list ---\");    var adjList = new Array(n);    for (var i = 0; i < n; i++) {        adjList[i] = [];    }    for (var i = 0; i < n; i++) {        var temp = [];        for (var j = 0; j < n; j++) {            if (graph[i][j] == 1) {                temp.push(i);            }        }        adjList[i] = temp;    }    console.log(\"----> adj list obtention time = \", new Date() - adj_start, \" ms Length = \", adjList.length);    var testAdjMatrix = [[0, 1, 0, 1], [0, 0, 0, 1], [1, 0, 0, 0], [0, 0, 1, 0]];    var testAdjList = [[1, 3], [3], [0], [2]];    return adjList;}return generateAndPreprocessGraph(param1);}generateAndPreprocessGraph_wrapper;").execute(doPageRank,param1);
    }

    @CEntryPoint(name = "generateAndPreprocessGraph_entry")
    public static CCharPointer generateAndPreprocessGraph_entry(IsolateThread thread, int param1) throws Exception {
        Object retObj = generateAndPreprocessGraph(param1);
        Value val = Value.asValue(retObj);
        List<List<Double>> listD = val.as(DOUBLE_LIST);
        Double [][] doubleArray = listD.stream().map(l -> l.toArray(new Double[l.size()])).toArray(Double[][]::new);
        byte[] bytes = SGXSerializer.serialize(doubleArray);
        int len = bytes.length;
        CCharPointer bytePtr = SGXSerializer.getCharPointer(bytes);
        return bytePtr;
    }

    @CFunction
    public static native void doPageRank_proxy(int param1);
}
