function sayHello() {
    console.log('++++++++++++ Hello from javascript file +++++++++++');
}


function arraySum(array) {
    var sum = 0;
    for (var i = 0; i < array.length; i++) {
        sum += array[i];
    }
    return sum;
}


function readXData(n) {
    var array = new Array(n);
    for (var i = 0; i < array.length; i++) {
        array[i] = i;
    }
    return array;
}

function add(a, b) {
    console.log("adding two numbers");
    return a + b;
}

function funcA() {
    var sec_double_a = Polyglot.eval("secL", "sDouble(125.45)");
    var resC = 123 + 456 * 789 / 111 - sec_double_a;
    var resN = funcN(sec_double_a, 2);
    var retd = funcD(resC);
    var a = 4;


    var sec_int = Polyglot.eval("secL", "sInt(45)");
    var myArray = new Array(2).fill(0);

    myArray = addSecIntToArray(sec_int, myArray);
    var zero = myArray[0];

    console.log(">>>> myArray[0] after adding secInt 45: ", zero);

    console.log("!!!!!!!!!!!!!!!!>>>>>>>>>> about to readXData(4) >>>>>>>>>>>>");
    var arrayFromOutside = readXData(4);    
    console.log("!!!!!!!!!!!!!!!!>>>>>>>>>> readXData(4) done !! >>>>>>>>>>>>");
    console.log("----------------> arrayFromOutside now inside [0] = ",arrayFromOutside[0]);
      
    console.log(">>>> in secure funcA: printing array received from outside >>>>");
    console.log(arrayFromOutside);

    var sumOfUntrustedArray = arraySum(arrayFromOutside);
    console.log(">>>> sum of untrusted array elements = ", sumOfUntrustedArray);

    sayHello();

    console.log('resN in funcA is : ' + resN);
    return resN;
}

function addSecIntToArray(val, array) {
    array[0] = val;
    return array;
}

function funcD(paramD) {

    var res = funcN(paramD, 2);
    console.log('funcD res from N: ' + res);
    return res;
}

function funcN(param, n) {
    console.log('funcN: ' + param);
    return param * n;
}

function funcM(param, m) {
    console.log('funcM: ' + param);
    return param / m;
}



function sum_1(n) {
    return n;
}


function sum_2(n, m) {
    return n + m;
}


function sum_6(a, b, c, d, e, f) {
    var poly3_secInt = Polyglot.eval("secL", "sInt(333)");
    console.log("=======>>> in sum_6 ------>>>");
    s2 = sum_2(a, b) + sum_2(c, d) + sum_2(e, f);
    return s2;
}

sayHello();
var retd = funcD(5.0);
var reta = funcA();
console.log(">>>>>>>> end of example.js program >>>>>>");


