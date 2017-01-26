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
    $("img").click(function(){

    	var src = $(this).attr("src");
    	var urls = new Array();
    	$("img").each(function(key,obj){
    		urls[urls.length] = $(obj).attr("src");
    	});

    	console.log(JSON.stringify(urls));

    	app.onImageClick(src, urls);
    });
}