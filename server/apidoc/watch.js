var gaze = require('gaze');
var exec = require('child_process').exec;
var fs = require('fs');

function init() {
    // fs.mkdirSync('./api');
    fs.mkdirSync('./doc');
    // createConfigureFile();
    beginWatch();
}

function createConfigureFile() {
    var configure = {
        "name": "测试",
        "version": "0.0.1",
        "description": "API文档测试",
        "title": "API文档测试",
        "url": "http://localhost:8080",
        "sampleUrl": "http://localhost:8080",
        "template": {
            "forceLanguage": "zh-cn"
        }
    };
    // fs.writeFileSync('./api/apidoc.json', JSON.stringify(configure));
}

function beginWatch() {
    gaze('../src/main/java/**/*.java', function (error, watcher) {
        this.on('all', function (event, filepath) {
            console.log(filepath + ' was ' + event);
            runGeneartion();
        })
    });
}

function runGeneartion() {
    var com = exec('apidoc -i ../ -o ./doc ');
    com.stdout.on('data', function (data) {
        console.log("生成Api->" + data);
    });

    com.stderr.on('data', function (data) {
        console.log('生成错误啦->' + data);
    });
}

if (fs.existsSync('./doc')) {
    beginWatch();
} else {
    init();
}