/**
 * Created by Administrator on 2017/8/16 0016.
 */
;
(function ($, document) {
    $.fn.loading = function (options) {
        return this.each(function () {
            if (typeof options === "string") {
                if (options === "show") {
                    _show(this);
                } else if (options === "hide") {
                    _hide(this);
                }
            } else if (typeof options === "object") {
                var opts = $.extend({}, $.fn.loading.defaults, options);
                $.data(this, "loading", {opts: opts});
                _init(this);
            }
        });
    }

    function _show(target) {
        var loading = $(target).find(".loading");
        var opts=$.data(target, "loading").opts;
        loading.show(0,opts.show);
    }

    function _hide(target) {
        var loading = $(target).find(".loading");
        var opts=$.data(target, "loading").opts;
        loading.hide(0,opts.hide);
    }

    function _init(target) {
        $(target).css({"position": "relative"});
        var loading = $(target).children(".loading");
        if (loading.length == 0) {
            var text = $.data(target, "loading").opts.text;
            var icon = $.data(target, "loading").opts.icon;

            var $loading = $("<div></div>").css({"position": "absolute", "width": "100%", "height": "100%","display":"none"}).addClass("loading");
            var $mask = $("<div></div>").css({
                    "position": "absolute",
                    "width": "100%",
                    "height": "100%",
                    "background-color": "black",
                    "filter": "alpha(opacity = 30)",
                    "-moz-opacity": 0.3,
                    "-khtml- opacity": 0.3,
                    opacity: 0.3
                }
            );
            var $loading_bar = $("<div></div>").css({
                "width": "240px",
                "height": "50px",
                "background-color": "aliceblue",
                'position': 'relative',
                'margin-left': '-120px',
                'margin-top': '-50px',
                'left': '50%',
                'top': '50%',
                'border-radius': '5px'
            });
            var $loading_text=$("<div>"+text+"</div>").css({
                "position": "absolute",
                'margin-left': '-100px',
                'margin-top': '-16px',
                'top': '50%',
                'left': '50%',
                'width': '168px',
                'height': '32px',
                'line-height': '32px',
                'padding-left': '32px'
            });
            if(icon!=""){
                $loading_text.css("background","url("+icon+") no-repeat");
            }
            $loading_bar.append($loading_text);
            $loading.append($mask);
            $loading.append($loading_bar)
            $(target).prepend($loading);
        }
    }

    $.fn.loading.defaults = {
        text: "加载中......",
        icon: "",
        show: function () {

        },
        hide: function () {

        }
    }
})
($, document);