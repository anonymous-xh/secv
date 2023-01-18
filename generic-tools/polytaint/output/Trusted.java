/*
* This file was generated by PolyTaint code partitioner - ERO project 2022
*
* The MIT License (MIT)
* Copyright (c) 2022 anonymous-xh anonymous-xh
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

public class Trusted {

    public static final TypeLiteral<List<Double>> SIMPLE_LIST = new TypeLiteral<List<Double>>() {};
    public static final TypeLiteral<List<List<Double>>> DOUBLE_LIST = new TypeLiteral<List<List<Double>>>() {};
    public static void main(String[] args) {
        System.out.println("Trusted dummy main!!");
    }
    public static void doPageRank(int param1){
        Context context = Context.newBuilder().allowAllAccess(true).build();
        Value generateAndPreprocessGraph = context.asValue(Trusted.class).getMember("static").getMember("generateAndPreprocessGraph");
        context.eval("js","function doPageRank_wrapper(generateAndPreprocessGraph,param1){function doPageRank(data_size) {    var sec_int = Polyglot.eval(\"secL\", \"sInt(0)\");    var damping = 0.85;    var personalised = undefined;    var maxIteration = 5;    var threshold = 0.001;    var callback = undefined;    var start_data = new Date();    var alist = generateAndPreprocessGraph(data_size);    var stop_data = new Date();    var diff_data = stop_data - start_data;    console.log(\"pagerank data size: \", alist.length, \" generation time = \", diff_data, \" ms\");    var adjacencyList = new Array(data_size);    for (var k = 0; k < data_size; k++) {        var temp = [];        var curr = alist[k];        for (var j = 0; j < curr.length; j++) {            temp.push(curr[j]);        }        adjacencyList[k] = temp;    }    var start_rank = new Date();     var iteration = 1;    var totalNodes = adjacencyList.length;    var pagerankScores = new Array(totalNodes).fill(1);    var pagerankSum = 1.0 * totalNodes;    var nodes = new Array(totalNodes);    for (var i = 0; i < totalNodes; i++) {        nodes[i] = i;    }    const allNodes = nodes;    while (iteration < maxIteration) {        console.log(\"--------------- pagerank iteration: \", iteration);        var error = 0.0;        ++iteration;        var newPagerankScores = new Array(totalNodes).fill(0);        for (let u = 0; u < totalNodes; ++u) {            if (personalised === undefined) {                newPagerankScores[u] += (1 - damping) * 1.0 / totalNodes;            } else if (u in personalised) {                newPagerankScores[u] += (1 - damping) * personalised[u];            }            let neighbours = adjacencyList[u];            if (neighbours.length == 0) {                neighbours = allNodes;            }            for (let v of neighbours) {                newPagerankScores[v] += 1.0 * damping * pagerankScores[u] / neighbours.length;            }        }        pagerankSum = 0.0;        for (let u = 0; u < totalNodes; ++u) {            pagerankSum += newPagerankScores[u];            error = Math.max(error, Math.abs(newPagerankScores[u] - pagerankScores[u]));        }        pagerankScores = newPagerankScores;        if (error < threshold) {            break;        }    }    for (let u = 0; u < totalNodes; ++u) {        pagerankScores[u] /= pagerankSum;    }    var stop_rank = new Date();    var diff_rank = stop_rank - start_rank;    var total = diff_data + diff_rank;    console.log(\"pagerank algo done: run time = \", diff_rank, \" ms\");    console.log(\"pagerank total time (data + pagerank algo) = \", total, \" ms\");}doPageRank(param1);}doPageRank_wrapper;").execute(generateAndPreprocessGraph,param1);
    }

    public static Object generateAndPreprocessGraph(int param1) throws Exception {
        byte[] tempRetBuf = new byte[168041];
        CCharPointer retPtr = SGXSerializer.getCharPointer(tempRetBuf);
        generateAndPreprocessGraph_proxy(param1,retPtr,168041);
        Object objFromProxy = SGXSerializer.deserialize(tempRetBuf);
        Double[][] doubleArray = (Double[][]) objFromProxy;
        return Value.asValue(doubleArray);
    }

    @CEntryPoint(name = "doPageRank_entry")
    public static void doPageRank_entry(IsolateThread thread, int param1){
        doPageRank(param1);
    }

    @CFunction
    public static native CCharPointer generateAndPreprocessGraph_proxy(int param1,CCharPointer retPtr, int retLen);
}
