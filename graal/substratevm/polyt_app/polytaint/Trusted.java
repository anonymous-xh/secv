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
import org.graalvm.polyglot.*;

public class Trusted {

    public static void main(String[] args) {
        System.out.println("Trusted dummy main!!");
    }
    public static double funcA(){
        Context context = Context.newBuilder().allowAllAccess(true).build();
        Value funcD = context.asValue(Trusted.class).getMember("static").getMember("funcD");
        Value funcN = context.asValue(Trusted.class).getMember("static").getMember("funcN");
        Value sayHello = context.asValue(Trusted.class).getMember("static").getMember("sayHello");
        Value addSecIntToArray = context.asValue(Trusted.class).getMember("static").getMember("addSecIntToArray");
        Value arraySum = context.asValue(Trusted.class).getMember("static").getMember("arraySum");
        Value readXData = context.asValue(Trusted.class).getMember("static").getMember("readXData");
        return context.eval("js","function funcA_wrapper(funcD,funcN,sayHello,addSecIntToArray,arraySum,readXData){function funcA() {    var sec_double_a = Polyglot.eval(\"secL\", \"sDouble(125.45)\");    var resC = 123 + 456 * 789 / 111 - sec_double_a;    var resN = funcN(sec_double_a, 2);    var retd = funcD(resC);    var a = 4;    var sec_int = Polyglot.eval(\"secL\", \"sInt(45)\");    var myArray = new Array(2).fill(0);    myArray = addSecIntToArray(sec_int, myArray);    var zero = myArray[0];    console.log(\">>>> myArray[0] after adding secInt 45: \", zero);    console.log(\"!!!!!!!!!!!!!!!!>>>>>>>>>> about to readXData(4) >>>>>>>>>>>>\");    var arrayFromOutside = readXData(4);    console.log(\"!!!!!!!!!!!!!!!!>>>>>>>>>> readXData(4) done !! >>>>>>>>>>>>\");       console.log(\">>>> in secure funcA: printing array received from outside >>>>\");    console.log(arrayFromOutside);    var sumOfUntrustedArray = arraySum(arrayFromOutside);    console.log(\">>>> sum of untrusted array elements = \", sumOfUntrustedArray);    sayHello();    console.log('resN in funcA is : ' + resN);    return resN;}return funcA();}funcA_wrapper;").execute(funcD,funcN,sayHello,addSecIntToArray,arraySum,readXData).asDouble();
    }

    public static double funcD(double param1){
        Context context = Context.newBuilder().allowAllAccess(true).build();
        Value funcA = context.asValue(Trusted.class).getMember("static").getMember("funcA");
        Value funcN = context.asValue(Trusted.class).getMember("static").getMember("funcN");
        Value sayHello = context.asValue(Trusted.class).getMember("static").getMember("sayHello");
        Value addSecIntToArray = context.asValue(Trusted.class).getMember("static").getMember("addSecIntToArray");
        Value arraySum = context.asValue(Trusted.class).getMember("static").getMember("arraySum");
        Value readXData = context.asValue(Trusted.class).getMember("static").getMember("readXData");
        return context.eval("js","function funcD_wrapper(funcA,funcN,sayHello,addSecIntToArray,arraySum,readXData,param1){function funcD(paramD) {    var res = funcN(paramD, 2);    console.log('funcD res from N: ' + res);    return res;}return funcD(param1);}funcD_wrapper;").execute(funcA,funcN,sayHello,addSecIntToArray,arraySum,readXData,param1).asDouble();
    }

    public static double funcN(double param1, int param2){
        Context context = Context.newBuilder().allowAllAccess(true).build();
        Value funcD = context.asValue(Trusted.class).getMember("static").getMember("funcD");
        Value funcA = context.asValue(Trusted.class).getMember("static").getMember("funcA");
        Value sayHello = context.asValue(Trusted.class).getMember("static").getMember("sayHello");
        Value addSecIntToArray = context.asValue(Trusted.class).getMember("static").getMember("addSecIntToArray");
        Value arraySum = context.asValue(Trusted.class).getMember("static").getMember("arraySum");
        Value readXData = context.asValue(Trusted.class).getMember("static").getMember("readXData");
        return context.eval("js","function funcN_wrapper(funcD,funcA,sayHello,addSecIntToArray,arraySum,readXData,param1,param2){function funcN(param, n) {    console.log('funcN: ' + param);    return param * n;}return funcN(param1, param2);}funcN_wrapper;").execute(funcD,funcA,sayHello,addSecIntToArray,arraySum,readXData,param1,param2).asDouble();
    }

    public static Object addSecIntToArray(int param1, Object param2){
        Context context = Context.newBuilder().allowAllAccess(true).build();
        Value funcD = context.asValue(Trusted.class).getMember("static").getMember("funcD");
        Value funcA = context.asValue(Trusted.class).getMember("static").getMember("funcA");
        Value funcN = context.asValue(Trusted.class).getMember("static").getMember("funcN");
        Value sayHello = context.asValue(Trusted.class).getMember("static").getMember("sayHello");
        Value arraySum = context.asValue(Trusted.class).getMember("static").getMember("arraySum");
        Value readXData = context.asValue(Trusted.class).getMember("static").getMember("readXData");
        Value param_val2 = Value.asValue(param2);
        return context.eval("js","function addSecIntToArray_wrapper(funcD,funcA,funcN,sayHello,arraySum,readXData,param1,param_val2){function addSecIntToArray(val, array) {    array[0] = val;    return array;}return addSecIntToArray(param1, param_val2);}addSecIntToArray_wrapper;").execute(funcD,funcA,funcN,sayHello,arraySum,readXData,param1,param_val2);
    }

    public static void sayHello(){
        sayHello_proxy();
    }

    public static int arraySum(Object param1) throws Exception {
        byte[] bytes_param1 = SGXSerializer.serialize(param1);
        int len_1 = bytes_param1.length;
        CCharPointer ptr_param1 = SGXSerializer.getCharPointer(bytes_param1);
        return arraySum_proxy(ptr_param1,len_1);
    }

    public static Object readXData(int param1){
        CCharPointer proxyPtr = readXData_proxy(param1);
        byte[] proxyBytes = SGXSerializer.getByteBuffer(proxyPtr, 256 * 1024 * 1024);
        Object objFromProxy = SGXSerializer.deserialize(proxyBytes);
        return Value.asValue(objFromProxy);
    }

    @CEntryPoint(name = "funcA_entry")
    public static double funcA_entry(IsolateThread thread){
        return funcA();
    }

    @CFunction
    public static native void sayHello_proxy();
    @CFunction
    public static native int arraySum_proxy(CCharPointer param1,int len1);
    @CFunction
    public static native CCharPointer readXData_proxy(int param1);
}
