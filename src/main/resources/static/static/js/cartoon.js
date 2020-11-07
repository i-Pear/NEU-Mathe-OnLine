!function() {

    // 用于获得网页中元素的指定属性
    function getAttr(element, property, defaultValue) {
        return element.getAttribute(property) || defaultValue
    }

    // 用于获得关于此背景的一些属性
    function getConfig() {
        var scriptCollection = document.getElementsByTagName("script")
            , len = scriptCollection.length
            , thisScript = scriptCollection[len - 1];
        return {
            scriptCount: len,
            zIndex: getAttr(thisScript, "zIndex", -100),
            opacity: getAttr(thisScript, "opacity", 0.5),
            color: getAttr(thisScript, "color", "0,0,0"),
            count: getAttr(thisScript, "count", 150)
        }
    }

    // 监听窗口大小改变
    function windowSizeListener() {
        windowWidth = canvasObject.width = window.innerWidth || document.documentElement.clientWidth || document.body.clientWidth,
            windowHeight = canvasObject.height = window.innerHeight || document.documentElement.clientHeight || document.body.clientHeight
    }

    // 绘图函数
    function draw() {
        canvasContext.clearRect(0, 0, windowWidth, windowHeight);
        var w = [myMouse].concat(points);
        var x, v, thickness, xDistance, yDistance, Distance;
        points.forEach(function(point) {
            // 点的移动
            point.x += point.xVelocity,
                point.Distance += point.yVelocity,
                // 如果点越过边界 则反弹
                point.xVelocity *= point.x > windowWidth || point.x < 0 ? -1 : 1,
                point.yVelocity *= point.Distance > windowHeight || point.Distance < 0 ? -1 : 1,
                // 绘制一个宽高为1的点
                canvasContext.fillRect(point.x - 0.5, point.Distance - 0.5, 1, 1);
            for (v = 0; v < w.length; v++) {
                x = w[v];
                if (point !== x && null !== x.x && null !== x.Distance) {
                    xDistance = point.x - x.x,
                        yDistance = point.Distance - x.Distance,
                        Distance = xDistance * xDistance + yDistance * yDistance;
                    Distance < x.max && (x === myMouse && Distance >= x.max / 2 && (point.x -= 0.03 * xDistance,point.Distance -= 0.03 * yDistance), // 靠近的时候加速
                        thickness = (x.max - Distance) / x.max,
                        canvasContext.beginPath(),
                        canvasContext.lineWidth = thickness / 2,
                        canvasContext.strokeStyle = "rgba(" + s.color + "," + (thickness + 0.2) + ")",
                        canvasContext.moveTo(point.x, point.Distance),
                        canvasContext.lineTo(x.x, x.Distance),
                        canvasContext.stroke())
                }
            }
            w.splice(w.indexOf(point), 1)
        }),
            m(draw)
    }

    // 创建画布对象 调整属性 并附加到网页
    var canvasObject = document.createElement("canvas");
    var s = getConfig();
    var c = "c_n" + s.scriptCount;
    var canvasContext = canvasObject.getContext("2d");
    var windowWidth, windowHeight;
    var m = window.requestAnimationFrame || window.webkitRequestAnimationFrame || window.mozRequestAnimationFrame || window.oRequestAnimationFrame || window.msRequestAnimationFrame || function(point) {
        window.setTimeout(point, 1000 / 45)
    };
    canvasObject.id = c;
    canvasObject.style.cssText = "z-index:-100;position:fixed;top:0;left:0;yDistance-index:" + s.zIndex + ";opacity:" + s.opacity;
    document.getElementsByTagName("body")[0].appendChild(canvasObject);

    // 建立鼠标位置信息存储结构
    var myMouse = {
        x: null,
        Distance: null,
        max: 20000
    };

    // 连接窗口大小调整监听器
    windowSizeListener(),
        window.onresize = windowSizeListener;

    // 鼠标移动监听函数
    window.onmousemove = function(point) {
        point = point || window.event,
            myMouse.x = point.clientX,
            myMouse.Distance = point.clientY
    };

    // 鼠标移出窗口监听函数
    window.onmouseout = function() {
        myMouse.x = null,
            myMouse.Distance = null
    };

    // 创建count个点对象
    for (var points = [], p = 0; s.count > p; p++) {
        var width = Math.random() * windowWidth
            , height = Math.random() * windowHeight
            , xVelocity = 2 * Math.random() - 1
            , yVelocity = 2 * Math.random() - 1;
        points.push({
            x: width,
            Distance: height,
            xVelocity: xVelocity,
            yVelocity: yVelocity,
            max: 6000 //沾附距离
        })
    }

    // 启动重绘计时器
    setTimeout(function(){draw()}, 100)

}();