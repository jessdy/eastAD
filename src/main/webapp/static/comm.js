(function($) {
	$.fn.paging = function(data, callback) {
		var obj = $(this);
		var totalPage = data.totalPages, totalCount = data.totalElements, pageSize = data.size, currentPage = data.number+1;

		var html = ['<div class="dataTables_info">共 ',totalCount,' 条</div><div class="dataTables_paginate paging_simple_numbers" id="paginate">'];
		html.push('<a class="paginate_button previous">上一页</a>');
		if (currentPage > 3) {
			html.push('<a hre="#" class="paging-first paginate_button">1</a><a hre="#" class="paging-omit paginate_button">...</a>');
		}
		for (var i = -2; i < 3; i++) {
			if (currentPage + i > 0 && currentPage + i <= totalPage) {
				html.push('<a hre="#" class="paging-middle paginate_button '
						+ (i == 0 ? 'current' : '') + '">'
						+ (currentPage + i) + '</a>');
			}
		}
		if (totalPage - currentPage > 2) {
			html
					.push('<a hre="#" class="paginate_button">...</a><a hre="#" class="paginate_button paging-last">'
							+ totalPage + '</a>');
		}
		html.push('<a href="#" class="paginate_button paging-next">下一页</a>');
		html.push('<input type="text" name="wanttopage" class="wanttopage" style="display:inline-block" value="'+currentPage+'" /><button class="paging-btn paginate_button">跳转</button>')
		html.push('</div>')
		obj.html(html.join(""));

		$(".paging-prev").click(function() {
			$("input[name=wangtopage]").val(currentPage);
			if (currentPage - 1 < 1) {
				callback(1, pageSize);
			} else {
				callback(currentPage - 1, pageSize);
			}
		});

		$(".paging-next").click(function() {
			if (currentPage + 1 > totalPage) {
				callback(totalPage, pageSize);
			} else {
				callback(currentPage + 1, pageSize);
			}
		});

		$(".paging-middle,.paging-first,.paging-last").click(function() {
			callback($(this).html(), pageSize);
		});
		
		$(".wanttopage").change(function(){
			if(isNaN($(this).val())){
				$(this).val(1);
			}
		}).keydown(function(e){
			if(isNaN($(this).val())){
				$(this).val(1);
			}
			if(e.keyCode == 13) {
				gotoPage($(this).val());
			}
		});
		$("button.paging-btn").click(function(){
			gotoPage($(".wanttopage").val());
		});
		
		function gotoPage(page){
			if(page > totalPage) {
				callback(totalPage, pageSize);
			} else if(page < 1) {
				callback(1, pageSize);
			} else {
				callback(page, pageSize);
			}
		}
	}
})($);

Date.prototype.Format = function (fmt) { //author: meizz 
    var o = {
        "M+": this.getMonth() + 1, //月份 
        "d+": this.getDate(), //日 
        "h+": this.getHours(), //小时 
        "m+": this.getMinutes(), //分 
        "s+": this.getSeconds(), //秒 
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度 
        "S": this.getMilliseconds() //毫秒 
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
    if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}

if(typeof(juicer) != 'undefined') {
	juicer.register('date', function(v) {
		return new Date(v).Format('yyyy-MM-dd hh:mm:ss');
	});
	juicer.register('onlydate', function(v) {
		return new Date(v).Format('yyyy-MM-dd');
	});
	juicer.register('newsstate', function(v) {
		switch(v) {
		case 0: 
			return '未审核';
		case 1: 
			return '已通过';
		case 2: 
			return '已结束';
		}
	});
}
$.getParam = function(name) {
	var url = encodeURI(location.href);
	var hasekey = url.indexOf(name);
	if (hasekey > 0) {
		var index = url.indexOf("?");
		var paramstr = "";
		if (index > 0 && url.length > index) {
			paramstr = url.substring(index);
		}
		var hasekeyP = paramstr.indexOf("?" + name + "=");
		if (hasekeyP < 0) {
			hasekeyP = paramstr.indexOf("&" + name + "=");
		}
		paramstr = paramstr.substring(1);
		if (hasekeyP >= 0) {
			var params = paramstr.split("&");
			for (var i = 0; params.length; i++) {
				if (params[i] != null) {
					var arr = params[i].split("=");
					if (arr.length > 1 && arr[0] == name)
						return arr[1].replace("#", "");
				}
			}
		}

	}
	return "";
}
$.redirect = function(url) {
	location.href = url;
}
$.districts = {
	'310101' : '黄浦区',
	'310104' : '徐汇区',
	'310105' : '长宁区',
	'310106' : '静安区',
	'310107' : '普陀区',
	'310108' : '闸北区',
	'310109' : '虹口区',
	'310110' : '杨浦区',
	'310112' : '闵行区',
	'310113' : '宝山区',
	'310114' : '嘉定区',
	'310115' : '浦东新区',
	'310116' : '金山区',
	'310117' : '松江区',
	'310118' : '青浦区',
	'310120' : '奉贤区'
}