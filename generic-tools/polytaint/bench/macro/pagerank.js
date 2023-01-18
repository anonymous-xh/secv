
function generateAndPreprocessGraph(n) {
    console.log(">>>>>>> generating directed RMAT graph with : ", n, " nodes and ", n * 2, " edges >>>>>>>");

    var edgesToGenerate = n * 2;

    var gen_start = new Date();

    var graph = new Array(n);
    for (var i = 0; i < n; i++) {
        graph[i] = new Array(n);
        graph[i].fill(0);
    }


    var pA = 0.45; var pB = 0.15; var pC = 0.15; var pD = 0.25;
    var createdEdges = 0;
    var nEdgesATime = edgesToGenerate;
    var cumA = pA; var cumB = cumA + pB; var cumC = cumB + pC; var cumD = 1.0;

    var fromIds = new Array(ne);
    var toIds = new Array(ne);

    while (edgesToGenerate > createdEdges) {

        var ne = Math.min((edgesToGenerate - createdEdges), nEdgesATime);

        for (var j = 0; j < ne; j++) {
            var col_st = 0; var col_en = n - 1; var row_st = 0; var row_en = n - 1;
            while (col_st != col_en || row_st != row_en) {
                var x = Math.random();

                if (x < cumA) {

                    col_en = col_st + (col_en - col_st) / 2;
                    row_en = row_st + (row_en - row_st) / 2;
                } else if (x < cumB) {

                    col_st = col_en - (col_en - col_st) / 2;
                    row_en = row_st + (row_en - row_st) / 2;

                } else if (x < cumC) {

                    col_en = col_st + (col_en - col_st) / 2;
                    row_st = row_en - (row_en - row_st) / 2;
                } else {

                    col_st = col_en - (col_en - col_st) / 2;
                    row_st = row_en - (row_en - row_st) / 2;
                }
            }
            fromIds[j] = Math.floor(col_st);
            toIds[j] = Math.floor(row_st);
        }

        createdEdges += ne;

    }

    console.log("----> graph generation time = ", new Date() - gen_start, " ms");

    var adj_start = new Date();
    console.log("--- converting graph to adjacency matrix ---");

    for (var i = 0; i < edgesToGenerate; i++) {
        var x = fromIds[i];
        var y = toIds[i];
        graph[x][y] = 1;
    }


    console.log("--- converting adjacency matrix to list ---");
    var adjList = new Array(n);
    for (var i = 0; i < n; i++) {
        adjList[i] = [];
    }

    for (var i = 0; i < n; i++) {
        var temp = [];
        for (var j = 0; j < n; j++) {
            if (graph[i][j] == 1) {
                temp.push(i);
            }

        }

        adjList[i] = temp;
    }

    console.log("----> adj list obtention time = ", new Date() - adj_start, " ms Length = ", adjList.length);

    var testAdjMatrix = [[0, 1, 0, 1], [0, 0, 0, 1], [1, 0, 0, 0], [0, 0, 1, 0]];
    var testAdjList = [[1, 3], [3], [0], [2]];

    return adjList;

}



function doPageRank(data_size) {

    var sec_int = Polyglot.eval("secL", "sInt(0)");
    var damping = 0.85;
    var personalised = undefined;
    var maxIteration = 5;
    var threshold = 0.001;
    var callback = undefined;


    var start_data = new Date();
    var alist = generateAndPreprocessGraph(data_size);

    var stop_data = new Date();
    var diff_data = stop_data - start_data;
    console.log("pagerank data size: ", alist.length, " generation time = ", diff_data, " ms");


    var adjacencyList = new Array(data_size);
    for (var k = 0; k < data_size; k++) {
        var temp = [];
        var curr = alist[k];
        for (var j = 0; j < curr.length; j++) {
            temp.push(curr[j]);
        }
        adjacencyList[k] = temp;
    }


    var start_rank = new Date();

 
    var iteration = 1;
    var totalNodes = adjacencyList.length;
    var pagerankScores = new Array(totalNodes).fill(1);
    var pagerankSum = 1.0 * totalNodes;

    var nodes = new Array(totalNodes);
    for (var i = 0; i < totalNodes; i++) {
        nodes[i] = i;
    }

    const allNodes = nodes;

    while (iteration < maxIteration) {
        console.log("--------------- pagerank iteration: ", iteration);
        var error = 0.0;
        ++iteration;
        var newPagerankScores = new Array(totalNodes).fill(0);
        for (let u = 0; u < totalNodes; ++u) {
            if (personalised === undefined) {
                newPagerankScores[u] += (1 - damping) * 1.0 / totalNodes;
            } else if (u in personalised) {
                newPagerankScores[u] += (1 - damping) * personalised[u];
            }
            let neighbours = adjacencyList[u];
            if (neighbours.length == 0) {
                neighbours = allNodes;
            }
            for (let v of neighbours) {
                newPagerankScores[v] += 1.0 * damping * pagerankScores[u] / neighbours.length;
            }
        }
        pagerankSum = 0.0;
        for (let u = 0; u < totalNodes; ++u) {
            pagerankSum += newPagerankScores[u];
            error = Math.max(error, Math.abs(newPagerankScores[u] - pagerankScores[u]));
        }
        pagerankScores = newPagerankScores;
        if (error < threshold) {
            break;
        }
    }

    for (let u = 0; u < totalNodes; ++u) {
        pagerankScores[u] /= pagerankSum;
    }

    var stop_rank = new Date();
    var diff_rank = stop_rank - start_rank;

    var total = diff_data + diff_rank;
    console.log("pagerank algo done: run time = ", diff_rank, " ms");
    console.log("pagerank total time (data + pagerank algo) = ", total, " ms");
}

doPageRank(4000);