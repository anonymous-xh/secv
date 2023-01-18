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
import org.graalvm.polyglot.*;

public class Untrusted {

    public static void main(String[] args) {
        System.out.println("Main: untrusted partition!!");
        Context context = Context.newBuilder().allowAllAccess(true).build();
        Value funcD = context.asValue(Untrusted.class).getMember("static").getMember("funcD");
        Value funcA = context.asValue(Untrusted.class).getMember("static").getMember("funcA");
        Value funcN = context.asValue(Untrusted.class).getMember("static").getMember("funcN");
        Value sayHello = context.asValue(Untrusted.class).getMember("static").getMember("sayHello");
        Value addSecIntToArray = context.asValue(Untrusted.class).getMember("static").getMember("addSecIntToArray");
        Value arraySum = context.asValue(Untrusted.class).getMember("static").getMember("arraySum");
        Value readXData = context.asValue(Untrusted.class).getMember("static").getMember("readXData");
        context.eval("js","function main_wrapper(funcD,funcA,funcN,sayHello,addSecIntToArray,arraySum,readXData){sayHello();var retd = funcD(5.0);var reta = funcA();console.log(\">>>>>>>> end of example.js program >>>>>>\");}main_wrapper;").execute(funcD,funcA,funcN,sayHello,addSecIntToArray,arraySum,readXData);
    }
    public static double funcA(){
        return funcA_proxy();
    }

    public static double funcD(double param1){
        Context context = Context.newBuilder().allowAllAccess(true).build();
        Value funcA = context.asValue(Untrusted.class).getMember("static").getMember("funcA");
        Value funcN = context.asValue(Untrusted.class).getMember("static").getMember("funcN");
        Value sayHello = context.asValue(Untrusted.class).getMember("static").getMember("sayHello");
        Value addSecIntToArray = context.asValue(Untrusted.class).getMember("static").getMember("addSecIntToArray");
        Value arraySum = context.asValue(Untrusted.class).getMember("static").getMember("arraySum");
        Value readXData = context.asValue(Untrusted.class).getMember("static").getMember("readXData");
        return context.eval("js","function funcD_wrapper(funcA,funcN,sayHello,addSecIntToArray,arraySum,readXData,param1){function funcD(paramD) {    var res = funcN(paramD, 2);    console.log('funcD res from N: ' + res);    return res;}return funcD(param1);}funcD_wrapper;").execute(funcA,funcN,sayHello,addSecIntToArray,arraySum,readXData,param1).asDouble();
    }

    public static double funcN(double param1, int param2){
        Context context = Context.newBuilder().allowAllAccess(true).build();
        Value funcD = context.asValue(Untrusted.class).getMember("static").getMember("funcD");
        Value funcA = context.asValue(Untrusted.class).getMember("static").getMember("funcA");
        Value sayHello = context.asValue(Untrusted.class).getMember("static").getMember("sayHello");
        Value addSecIntToArray = context.asValue(Untrusted.class).getMember("static").getMember("addSecIntToArray");
        Value arraySum = context.asValue(Untrusted.class).getMember("static").getMember("arraySum");
        Value readXData = context.asValue(Untrusted.class).getMember("static").getMember("readXData");
        return context.eval("js","function funcN_wrapper(funcD,funcA,sayHello,addSecIntToArray,arraySum,readXData,param1,param2){function funcN(param, n) {    console.log('funcN: ' + param);    return param * n;}return funcN(param1, param2);}funcN_wrapper;").execute(funcD,funcA,sayHello,addSecIntToArray,arraySum,readXData,param1,param2).asDouble();
    }

    public static Object addSecIntToArray(int param1, Object param2){
        Context context = Context.newBuilder().allowAllAccess(true).build();
        Value funcD = context.asValue(Untrusted.class).getMember("static").getMember("funcD");
        Value funcA = context.asValue(Untrusted.class).getMember("static").getMember("funcA");
        Value funcN = context.asValue(Untrusted.class).getMember("static").getMember("funcN");
        Value sayHello = context.asValue(Untrusted.class).getMember("static").getMember("sayHello");
        Value arraySum = context.asValue(Untrusted.class).getMember("static").getMember("arraySum");
        Value readXData = context.asValue(Untrusted.class).getMember("static").getMember("readXData");
        Value param_val2 = Value.asValue(param2);
        return context.eval("js","function addSecIntToArray_wrapper(funcD,funcA,funcN,sayHello,arraySum,readXData,param1,param_val2){function addSecIntToArray(val, array) {    array[0] = val;    return array;}return addSecIntToArray(param1, param_val2);}addSecIntToArray_wrapper;").execute(funcD,funcA,funcN,sayHello,arraySum,readXData,param1,param_val2);
    }

    public static void sayHello(){
        Context context = Context.newBuilder().allowAllAccess(true).build();
        Value funcD = context.asValue(Untrusted.class).getMember("static").getMember("funcD");
        Value funcA = context.asValue(Untrusted.class).getMember("static").getMember("funcA");
        Value funcN = context.asValue(Untrusted.class).getMember("static").getMember("funcN");
        Value addSecIntToArray = context.asValue(Untrusted.class).getMember("static").getMember("addSecIntToArray");
        Value arraySum = context.asValue(Untrusted.class).getMember("static").getMember("arraySum");
        Value readXData = context.asValue(Untrusted.class).getMember("static").getMember("readXData");
        context.eval("js","function sayHello_wrapper(funcD,funcA,funcN,addSecIntToArray,arraySum,readXData){function sayHello() {    console.log('++++++++++++ Hello from javascript file +++++++++++');}sayHello();}sayHello_wrapper;").execute(funcD,funcA,funcN,addSecIntToArray,arraySum,readXData);
    }

    public static int arraySum(Object param1){
        Context context = Context.newBuilder().allowAllAccess(true).build();
        Value funcD = context.asValue(Untrusted.class).getMember("static").getMember("funcD");
        Value funcA = context.asValue(Untrusted.class).getMember("static").getMember("funcA");
        Value funcN = context.asValue(Untrusted.class).getMember("static").getMember("funcN");
        Value sayHello = context.asValue(Untrusted.class).getMember("static").getMember("sayHello");
        Value addSecIntToArray = context.asValue(Untrusted.class).getMember("static").getMember("addSecIntToArray");
        Value readXData = context.asValue(Untrusted.class).getMember("static").getMember("readXData");
        Value param_val1 = Value.asValue(param1);
        return context.eval("js","function arraySum_wrapper(funcD,funcA,funcN,sayHello,addSecIntToArray,readXData,param_val1){function arraySum(array) {    var sum = 0;    for (var i = 0; i < array.length; i++) {        sum += array[i];    }    return sum;}return arraySum(param_val1);}arraySum_wrapper;").execute(funcD,funcA,funcN,sayHello,addSecIntToArray,readXData,param_val1).asInt();
    }

    public static Object readXData(int param1){
        Context context = Context.newBuilder().allowAllAccess(true).build();
        Value funcD = context.asValue(Untrusted.class).getMember("static").getMember("funcD");
        Value funcA = context.asValue(Untrusted.class).getMember("static").getMember("funcA");
        Value funcN = context.asValue(Untrusted.class).getMember("static").getMember("funcN");
        Value sayHello = context.asValue(Untrusted.class).getMember("static").getMember("sayHello");
        Value addSecIntToArray = context.asValue(Untrusted.class).getMember("static").getMember("addSecIntToArray");
        Value arraySum = context.asValue(Untrusted.class).getMember("static").getMember("arraySum");
        return context.eval("js","function readXData_wrapper(funcD,funcA,funcN,sayHello,addSecIntToArray,arraySum,param1){function readXData(n) {    var array = new Array(n);    for (var i = 0; i < array.length; i++) {        array[i] = i;    }    return array;}return readXData(param1);}readXData_wrapper;").execute(funcD,funcA,funcN,sayHello,addSecIntToArray,arraySum,param1);
    }

    @CEntryPoint(name = "sayHello_entry")
    public static void sayHello_entry(IsolateThread thread){
    }

    @CEntryPoint(name = "arraySum_entry")
    public static int arraySum_entry(IsolateThread thread, CCharPointer param1,int len1){
        byte[] bytes_1 = SGXSerializer.getByteBuffer(param1, len1);
        Object obj_param1 = SGXSerializer.deserialize(bytes_1);
        return arraySum(obj_param1);
    }

    @CEntryPoint(name = "readXData_entry")
    public static CCharPointer readXData_entry(IsolateThread thread, int param1) throws Exception {
        Object retObj = readXData(param1);
        byte[] bytes = SGXSerializer.serialize(retObj);
        int len = bytes.length;
        byte[] tempRetBuf = new byte[256 * 1024 * 1024];
        SGXSerializer.arrayCopy(tempRetBuf, bytes, len);
        CCharPointer retPtr = SGXSerializer.getCharPointer(tempRetBuf);
        return retPtr;
    }

    @CFunction
    public static native double funcA_proxy();
}
