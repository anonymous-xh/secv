



const {readFileSync, promises: fsPromises} = require('fs');


function syncReadFile(filename) {
  const contents = readFileSync(filename, 'utf-8');

  const arr = contents.split(/\r?\n/);

  console.log(arr); 

  return arr;
}

syncReadFile('./hello.txt');