$(function() {
	$.modal.prototype.defaults.modalContainer = document.body
	
	$.ajax({
		type: "get",
		url: "../services/weixin/app2/info",
		success: function(data) {
			if(data.type == 'company') {
				$("#infolink").show();
				if(data.vcompany.status == 0) {
					$("#infolink .index-title").append('<label style="font-size: 0.5rem;margin-left:0.5rem;color:red;">审核中</label>');
				} else if(data.vcompany.status == -1) {
					$("#infolink").remove();
					$("#infolinkerr").show();
				} else {
					$("#teamlist").show();
					$("#eventlist").show();
					$("#infolink .index-title").append('<label style="font-size: 0.5rem;margin-left:0.5rem;color:green;">认证</label>');
				}
				for(var i in data.vcompany) {
					$("#v-" + i).html(data.vcompany[i]);
				}
				$("#pic1p").attr("src", data.vcompany.pic1);
				$("#pic2p").attr("src", data.vcompany.pic2);
			} else if(data.type == 'person') {
				if(data.status == -1) {
					$("#infolinkerr2").show();
				} else if(data.status == 1 || data.status == 0) {
					$("#infolink2").show();
					for(var i in data) {
						$("#p-" + i).html(data[i]);
					}
					$("#p-company").html(data.vcompany.name)
					if(data.ismanager == 1) {
						$("#infolink").show();
					}
				} else {
					$("#infolink2 .index-title").append('<label style="font-size: 0.5rem;margin-left:0.5rem;color:red;">审核中</label>');
				}
			} else {
				$("#joinlink").show();
			}
		}
	});
	
	$("#addTime").click(function(){
		$.ajax({
			url: '../services/eventjoin/' + window.localStorage['id'],
			type: 'post',
			data: {
				score: $('input[name=score]').val(),
				eventDate: $('select[name=eventDate]').val(),
				person: $('select[name=person]').val()
			},
			success: function(data) {
				// $.router.load('#eventjoin', true);
				$.router.back();
			}
		})
	})
});
$.pages = {
	about: function() {
		$.ajax({
			type: "get",
			url: "../aboutus",
			success: function(data) {
				$.hideIndicator();
				$("#about .content-padded").html(data.content);
			}
		});
	},
	event: function() {
		var loading = false;
		var pageno = 1;
		$('.infinite-scroll-bottom .list-container').html("");
		$('.infinite-scroll-preloader').show();

		function addItems() {
			loading = true;
			// 生成新条目的HTML
			var html = juicer($("#events-tpl").html());
			$.ajax({
				type: "post",
				cache: false,
				dataType: 'json',
				data: {
					page: pageno,
					rows: 10
				},
				url: "../event/publishlist",
				success: function(data) {
					// 添加新条目
					loading = false;
					if (data.content && data.content.length > 0) {
						$('.infinite-scroll-bottom .list-container').append(html.render(data));
						pageno++;
						if (data.content.length < 10) {
							// 加载完毕，则注销无限加载事件，以防不必要的加载
							$.detachInfiniteScroll($('.infinite-scroll'));
							// 删除加载提示符
							$('.infinite-scroll-preloader').hide();
						}
					} else {
						// 加载完毕，则注销无限加载事件，以防不必要的加载
						$.detachInfiniteScroll($('.infinite-scroll'));
						// 删除加载提示符
						$('.infinite-scroll-preloader').hide();
					}
					$.refreshScroller();
				}
			});
		}
		addItems();

		$("#event").on('infinite', '.infinite-scroll-bottom', function() {

			// 如果正在加载，则退出
			if (loading) return;
			addItems();
		});
	},
	detail: function() {
		var id = $.getParam("id");
		var companyJoin = function(id, data) {
			var buttons1 = [{
				text: '请选择活动日',
				label: true
			}];
			var eventDate = data.eventDate;
			if (eventDate) {
				var edates = eventDate.split(",");
				for (var i in edates) {
					buttons1.push({
						text: edates[i],
						onClick: function() {
							$.ajax({
								url: "../event/companyjoin/" + id,
								type: 'post',
								data: {
									date: edates[i]
								},
								success: function() {
									$(".opbtn").html('等待审核');
								}
							})
						}
					});
				}
			}
			var buttons2 = [{
				text: '取消',
				bg: 'danger'
			}];
			var groups = [buttons1, buttons2];
			$.actions(groups);
		}
		var personJoin = function(id) {
			$.ajax({
				url: "../event/personjoin/" + id,
				type: 'post',
				success: function() {
					$(".opbtn").html('等待审核');
				}
			})
		}
		$.ajax({
			url: "../event/" + id,
			success: function(data) {
				$(".article-title").html(data.name);
				$(".article-date").html(new Date(data.uptime).Format('yyyy-MM-dd'));
				$(".article-content").html(data.content);
				switch (data.canjoin) {
					case 1:
						$(".opbtn").html('申领活动').removeClass('disabled').click(function() {
							companyJoin(id, data);
						});
						break;
					case 2:
						$(".opbtn").removeClass('disabled').click(function() {
							personJoin(id);
						});
						break;
					case 3:
						$(".opbtn").html('已申领该活动');
						break;
					case 5:
						$(".opbtn").html('等待审核');
						break;
					case 4:
						$(".opbtn").html('已加入活动');
						break;
					case 6:
						$(".opbtn").html('等待审核');
						break;
				}
				$("#company-list tbody").html('');
				for (var i in data.companys) {
					$("#company-list tbody").append('<tr><td>' + data.companys[i] + '</td></tr>');
				}
			}
		});
	},
	news: function() {
		var map = new BMap.Map("map"); // 创建地图实例  
		map.addControl(new BMap.NavigationControl());
		map.clearOverlays();
		$('#company-list tbody').html('')
		var point = new BMap.Point(121.480386, 31.236559);
		map.centerAndZoom(point, 13);
		$.ajax({
			type: "post",
			url: "../news/publishlist",
			success: function(data) {
				for (var i in data) {
					for(var j in data[i].position.split(';')) {
						var position = data[i].position.split(';')[j].split(',');
						var p = new BMap.Point(position[0], position[1]);
						var marker = new BMap.Marker(p);
						map.addOverlay(marker);
					}
					
					$('#company-list tbody').append('<tr><td><a href="news-detail.html?id=' + data[i].id + '">' + data[i].name + '</td></tr>');
				}
			}
		});
	},
	newsdetail: function() {
		var id = $.getParam("id");
		$.ajax({
			url: "../news/" + id,
			success: function(data) {
				$(".article-title").html(data.name);
				$(".article-date").html(new Date(data.uptime).Format('yyyy-MM-dd'));
				$(".article-content").html(data.content);
			}
		});
	},
	myinfo: function(){
		$.ajax({
			url: "../services/events",
			success: function(data) {
				$("#myevent-list tbody").html('');
				for(var i in data) {
					$("#myevent-list tbody").append('<tr><td><a data-id="' + (data[i].canjoin == 1 ? data[i].id : "") + '">' + data[i].name + '<span style="float:right;padding-right: 1em;color: red">'+(data[i].canjoin == 1 ? '审核通过' : (data[i].canjoin == -1 ? '审核不通过' : '正在审核'))+'</span></a></td></tr>')
				}
			}
		})
	},
	myevent: function() {
		$.ajax({
			url: "../services/events",
			success: function(data) {
				$("#myevents").html('');
				for(var i in data) {
					$("#myevents").append('<tr><td><a data-id="' + (data[i].canjoin == 1 ? data[i].id : "") + '">' + data[i].name + '<span style="float:right;padding-right: 1em;color: red">'+(data[i].canjoin == 1 ? '审核通过' : (data[i].canjoin == -1 ? '审核不通过' : '正在审核'))+'</span></a></td></tr>')
				}
			}
		})
	},
	myteam: function() {
		var html = juicer($("#volunteer-tpl").html());
		$.ajax({
			url: "../services/myteam",
			success: function(data) {
				$('#teamvolunteers').html(html.render({content:data}));
			}
		})
	},
	eventjoin: function() {
		var html = juicer($("#eventjoin-tpl").html());
		$.ajax({
			url: '../services/eventjoin/' + window.localStorage['id'],
			success: function(data) {
				$('#addTimelist').html(html.render({content:data}));
			}
		})
	},
	eventtime: function() {
		$.ajax({
			url: '../event/' + window.localStorage['id'],
			success: function(data) {
				var events = data.eventDate.split(",");
				$("select[name=eventDate]").html("");
				for(var i in events) {
					$("select[name=eventDate]").append('<option>'+events[i]+'</option>');
				}
			}
		});
		$.ajax({
			url: "../services/myteam",
			success: function(data) {
				$('select[name=person]').html("");
				for(var i in data) {
					$('select[name=person]').append('<option value="' + data[i].id + '">' + data[i].name + '(' + data[i].vnum + ')</option>');
				}
			}
		});
	}
};