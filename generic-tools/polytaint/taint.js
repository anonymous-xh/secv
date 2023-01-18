

function funcA() {
    var sec_int = Polyglot.eval("secL", "sInt(45)");
    var x = sec_int + 2;
    var y = 4 + x;

    console.log(">>>>> funcA():: y = ", y);
}

var val = Polyglot.eval("secL", "sInt(45)");
funcA();