//ccl stands for cocolabel
if (!window.ccl) window.ccl={}
if (!ccl.net) ccl.net={}

ccl.globalSettings = {
	labelPathConfig : 'workspace.json',
	thumbPageSize : 20,
	minAutoSaveInterval : 10
}

ccl.resetDefaultSettings = function() {
	return {
		autoCommitOnSwitchImage:true,
		saveMode:'PascalVOC',
		thumbnailwidth : '80px',
		categories : ['person', 'dog', 'cat', 'flower']
	}
};
ccl.defaultSettings = ccl.resetDefaultSettings()

ccl.settings = {
	imgpath:"E:/aidata/imgclass/person/",
	workpath:"E:/aidata/imgclass/persontags/",
	labelmode:"PascalVOC"
}

ccl.baseURL = ""
ccl.slidetag = {}

ccl.project={}
ccl.project.init = function(callback) {
	
	ccl.net.loadImageDir(function(succ) {
		if(succ) {
			ccl.net.loadDefaultSettings();
			ccl.net.loadThumbnails();
			if (callback) callback(true);
		} else {
			if (callback) callback(false);
		}
	})
}

ccl.net.loadImageDir = function(callback) {
	imgpath = ccl.settings.imgpath
	
	if (!imgpath.endsWith("/")) imgpath += "/";

	$.ajax({
		url:ccl.baseURL+"imglist",
		data: {
			imgpath:imgpath
		},
		method:"get",
		success:function(data){
			ccl.currentImageList = JSON.parse(data);
			if (callback) callback(true);
		},
		fail:function(err){
			ccl.currentImageList = [];
			tag.thumbnails = [];
			console.log(err);
			alert("����ͼƬ�б�ʧ��")
			if (callback) callback(false);
		}
	})
}

ccl.net.loadThumbnails = function(){
	ccl.slidetag.thumbnails = []
	imgpath = ccl.settings.imgpath
	for ( var p in ccl.currentImageList) {
		var item = ccl.currentImageList[p];
		
		//	ccl.slidetag.thumbnails.push({name : item, src: "data:image/" + picdata.type +  ";base64,"+ picdata.data});
		//	ccl.slidetag.update();
		ccl.slidetag.thumbnails.push({name : item, src: ccl.baseURL+"img?imgfile=" + imgpath + item + "&w=" + ccl.defaultSettings.thumbnailwidth});
		ccl.slidetag.update();
	}
}

ccl.net.loadImage = function(imgname, callback, thumb) {
	w = 0;
	if (thumb) w = ccl.defaultSettings.thumbnailwidth;
	imgpath = ccl.settings.imgpath

	if (!imgpath.endsWith("/")) imgpath += "/";
	$.ajax({
		url:ccl.baseURL+"img",
		data: {
			imgfile:imgpath + imgname,
			w : w,
			mode : 1
		},
		method:"get",
		success:function(data){
			//{type:jpg,data:ABCD}
			var picdata = JSON.parse(data);
			callback(picdata);
			//$("<img/>").attr("src","data:image/" + picdata.type +  ";base64,"+ picdata.data).appendTo($('body'));
			
		},
		failure:function(){
		}
	})
}

ccl.net.loadDefaultSettings = function() {
    //'http://localhost:9999/loadDefaultSettings?workpath=E:/aidata/imgclass/person/'
    $.ajax({
        url:ccl.baseURL+"loadDefaultSettings",
        data: {
            workpath:ccl.settings.workpath
        },
        method:"get",
        success:function(data){
            tmpdefaultSettings = JSON.parse(data);
            if (tmpdefaultSettings.categories) {
                ccl.defaultSettings = tmpdefaultSettings
            } else {
                ccl.defaultSettings = ccl.resetDefaultSettings()
            }
        },
        failure:function(){
        }
    })
}

ccl.net.loadLabel = function(imgname, callback) {
	$.ajax({
		url:ccl.baseURL+"loadLabel",
		data: {
			workpath:ccl.settings.workpath,
			imgname : imgname
		},
		method:"post",
		success:function(data){
			//console.log(JSON.parse(data))
			if (callback) {
				callback(JSON.parse(data));
			}
		},
		failure:function(){
			if (callback) {
				callback();
			}
		}
	})
}

ccl.net.saveDefaultSettings = function(defaultSettings, callback) {

	if (!defaultSettings) defaultSettings = ccl.defaultSettings;
	$.ajax({
		url:ccl.baseURL+"saveDefaultSettings",
		data: {
			workpath:ccl.settings.workpath,
			settings:JSON.stringify(defaultSettings)
		},
		method:"get",
		success:function(data){
			ccl.defaultSettings = defaultSettings;
			if (callback ) callback(true);
		},
		failure:function(){
		if (callback ) callback(false);
		}
	})
}

ccl.net.saveLabel = function(labeldata, callback) {
	
	$.ajax({
		url:ccl.baseURL+"saveLabel",
		data: {
			workpath:ccl.settings.workpath,
			labeldata : JSON.stringify(labeldata)
		},
		method:"post",
		success:function(data){
			console.log(data)
			if (callback) {
				callback(true);
			}
		},
		failure:function(){
			if (callback) {
				callback(false);
			}
		}
	})
}