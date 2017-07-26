var hasJQuery = (typeof($) !="undefined");
if(hasJQuery){
	$(function(){
		onPageLoad();
	})
}else{
	var s = document.createElement("script");
	s.src="js/jquery.js";
	s.type="text/javascript";
	s.onload = function(){
		$(function(){onPageLoad();});
	}
	document.head.appendChild(s);
}

function onPageLoad(){

}

// 图片点击
function initImage(){
    $("img").click(function(e){
                   e.stopPropagation(); // 阻止事件冒泡
                	var src = $(this).attr("src");
                	var urls = new Array();
                	$("img").each(function(key,obj){
                		urls[urls.length] = $(obj).attr("src");
                	});
                	var images ="{}"
                    if(urls.length>0){
                        images = JSON.stringify(urls);
                    }
                	app.onImageClick(src, images);
                	return false;
                });
}