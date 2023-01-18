
function readXData(n) {
    var xdata = new Array(n);

    for (var i = 0; i < n; i++) {
        xdata[i] = Math.random();
    }

    var sum = 0;
    for (var i = 0; i < n; i++) {
        sum += xdata[i];
    }
    var mean = (sum / n);

    var std = 0;
    for (var i = 0; i < n; i++) {
        std += Math.pow(xdata[i] - mean, 2);
    }


    var dev = Math.sqrt(std / n);

    for (var i = 0; i < n; i++) {
        xdata[i] = (xdata[i] - mean) / dev;
    }

    return xdata;

}


function readYData(n) {
    var ydata = new Array(n);
    ydata.fill(Math.random());

    var rand = Math.random();

    for (var i = 0; i < n; i++) {
        ydata[i] *= rand;
    }

    var sum = 0;
    for (var i = 0; i < n; i++) {
        sum += ydata[i];
    }
    var mean = (sum / n);

    var std = 0;
    for (var i = 0; i < n; i++) {
        std += Math.pow(ydata[i] - mean, 2);
    }


    var dev = Math.sqrt(std / n);


    for (var i = 0; i < n; i++) {
        ydata[i] = (ydata[i] - mean) / dev;
    }



    return ydata;
}



function trainModel(num_iter) {
    //var m = Polyglot.eval("secL", "sDouble(0.0)");
    //var c = Polyglot.eval("secL", "sDouble(0.0)");
    var m = 0.0;
    var c = 0.0;

    var L = 0.0001;
    var N = 20000;

    var data_start = new Date();
    var X = readXData(N);
    var Y = readYData(N);
    var data_stop = new Date();
    var data_time = data_stop - data_start;
    console.log(">>>>>>>>>>>> Dataset size: ", N, "read time = ", data_time, " ms");

    var train_start = new Date();

    for (var j = 0; j < num_iter; j++) {


        var temp = new Array(N);
        for (var i = 0; i < N; i++) {
            temp[i] = m * X[i];
        }

        var Y_pred = new Array(N);
        for (var i = 0; i < N; i++) {
            Y_pred[i] = temp[i] + c;
        }

        var Y_diff = new Array(N);
        for (var i = 0; i < N; i++) {
            Y_diff[i] = Y[i] - Y_pred[i];

        }

        var XY_diff = new Array(N);
        for (var i = 0; i < N; i++) {
            XY_diff[i] = X[i] * Y_diff[i];
        }

        var sumxydiff = 0;
        for (var i = 0; i < N; i++) {
            sumxydiff += XY_diff[i];
        }

        var sumydiff = 0;
        for (var i = 0; i < N; i++) {
            sumydiff += Y_diff[i];
        }

        var D_m = (-2 / N) * sumxydiff;
        var D_c = (-2 / N) * sumydiff;

        m -= L * D_m;
        c -= L * D_c;

    }

    var train_stop = new Date();
    var train_time = train_stop - train_start;
    var total = data_time + train_time;

    console.log("Model after training: m = ", m, " c = ", c);
    console.log(">>>>>>>>>>>> Model training time = ", train_time, " ms");
    console.log(">>>>>>>>>>> Total (data-read + training) = ", total);
}


trainModel(100);




