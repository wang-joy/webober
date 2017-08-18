/**
 * Created by Administrator on 2017/8/10 0010.
 */
;
(function ($) {
    $.fn.page = function (options) {
        return this.each(function () {
            var opts = $.extend({}, $.fn.page.defaults, options);
            $.data(this, "page", {opts: opts});
            buildTable(this);
        });
    };

    function buildTable(target) {
        var opts = $.data(target, "page").opts;
        var columns = opts.columns;
        //列数
        var columnNum = columns.length;
        var tb = $(target).addClass("page").html("<table cellspacing=\"0\" cellpadding=\"0\" border=\"0\"><thead></thead><tbody></tbody></table>");
        $(target).append("<p></p>");
        var thead = tb.find("thead");
        var tr = "<tr>";
        for (var i = 0; i < columnNum; i++) {
            if(columns[i].checkbox){
                tr+="<th><input type='checkbox'></th>";
            }else{
                tr += "<th>" + columns[i].title + "</th>";
            }
        }
        thead.append(tr);
        _select(target, opts.pageIndex);
    }

    function _select(target, page) {
        var opts = $.data(target, "page").opts;
        var params = $.extend({}, opts.params, {page: page, pageSize: opts.pageSize});
        $.ajax({
            url: opts.url,
            dataType: "JSON",
            type: opts.type,
            data: params,
            async: false,
            success: function (data) {
                var total = data.total;
                var list = data.list;
                var tbody = $(target).find("tbody");
                console.log(tbody.length);
                var columns = opts.columns;
                //列数
                var columnNum = columns.length;
                //行数
                var rowNum = list.length;
                var trs = "";
                for (var j = 0; j < rowNum; j++) {
                    trs += "<tr>"
                    for (var k = 0; k < columnNum; k++) {
                        var formatter=columns[k].formatter;
                        var checkbox=columns[k].checkbox;
                        if(typeof formatter=="function"){
                            var val=formatter(j,list[j]);
                            trs += "<td>" + val + "</td>";
                        }else{
                            var field = columns[k].field;
                            if(checkbox){
                                trs+="<td><input type='checkbox' value='"+list[j][field]+"'></td>"
                            }else{
                                trs += "<td>" + list[j][field] + "</td>";
                            }
                        }
                    }
                    trs += "</tr>";
                }
                tbody.html(trs);
                $(target).find("thead input[type='checkbox']").click(function(){
                    var index=$(this).parents("th").index();
                    if(!$(this).is(":checked")){
                        $(this).attr("checked",false);
                        tbody.find("tr").each(function(){
                            $(this).find("input[type='checkbox']").eq(index).attr("checked",false);
                        });
                    }else{
                        $(this).attr("checked",true);
                        tbody.find("tr").each(function(){
                            $(this).find("input[type='checkbox']").eq(index).attr("checked",true);
                        });
                    }
                });
                console.log(data);
                if (total != 0) {
                    buildNavigatePages(target, page, total);
                }
            }
        });
    }

    function buildNavigatePages(target, page, total) {
        var p = $(target).find("p").empty();
        var opts = $.data(target, "page").opts;
        var pageNum = opts.navPageNum;
        var pageSize = opts.pageSize;
        var navigatePageNumbers=_calcNavigatePageNumbers(page,total,pageNum,pageSize);
        var prevPage=null;
        if(page==1){
            prevPage=$("<a href='javascript:void(0)'>"+opts.prevPage+"</a>");
            prevPage.addClass("not-allowed");
        }else{
            prevPage=$("<a href='javascript:void(0)'>"+opts.prevPage+"</a>");
            prevPage.click(function(){
                opts.onPrevPage(page);
                _select(target,page-1);
            })
        }
        if(prevPage!=null){
            prevPage.addClass("prev-page");
            p.append(prevPage);
        }
        $.each(navigatePageNumbers,function(index,page){
            var a = $("<a href='javascript:void(0)'>" + page + "</a>");
            a.click(function(){
                opts.onSelectPage(page);
                _select(target,page);
            });
            p.append(a);
        });
        var nextPage=null;
        var totalPages =  Math.floor((total - 1) / pageSize) + 1;
        if(page==totalPages){
            nextPage=$("<a href='javascript:void(0)'>"+opts.nextPage+"</a>");
            nextPage.addClass("not-allowed");
        }else{
            nextPage=$("<a href='javascript:void(0)'>"+opts.nextPage+"</a>");
            nextPage.click(function(){
                opts.onNextPage(page);
                _select(target,parseInt(page)+1);
            })
        }
        if(nextPage!=null){
            nextPage.addClass("next-page");
            p.append(nextPage);
        }
    }


    function _calcNavigatePageNumbers(page,total,navPageNum,pageSize){
        var totalPages = Math.floor((total - 1) / pageSize) + 1;
        var navigatePageNumbers=new Array();
        if(totalPages<=navPageNum){
            for(var i=0;i<totalPages;i++){
                navigatePageNumbers.push(i+1);
            }
        }else{
            var startNum=page-navPageNum/2;
            var endNum=page+navPageNum/2;
            if(startNum<1){
                startNum=1;
                for(var j=0;j<navPageNum;j++){
                    navigatePageNumbers.push(startNum++);
                }
            }else if(endNum>totalPages){
                endNum=totalPages;
                //最后navPageCount页
                for(var  k=navPageNum-1;k>=0;k--){
                    navigatePageNumbers.push(endNum--);
                }
            }else{
                for(var l=0;l<navPageNum;l++){
                    navigatePageNumbers.push(startNum++);
                }
            }
        }

        return navigatePageNumbers;
    }

    $.fn.page.defaults = {
        //当前页码
        pageIndex: 1,
        //每页记录数
        pageSize: 20,
        //上一页
        prevPage: "上一页",
        //下一页
        nextPage: "下一页",
        //请求方式
        method: "POST",
        params: {},
        navPageNum: 10,
        //点击链接时触发的事件 page为当前链接的页码
        onSelectPage: function (page) {

        },
        //点击上一页链接时触发事件，page为当前页
        onPrevPage: function (page) {

        },
        //点击下一页链接时触发事件，page为当前页
        onNextPage: function (page) {

        }
    }
})($);