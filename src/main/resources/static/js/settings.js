var appConfig = {
    autosave : {
        syncingInterval : 10 * 1000,  //10 seconds
        enable : true,
        deleteIfExported: true, //Mark the data as saved when exported as nimn format, and delte the copy from browser cache.
    },
    zoomStepSize: 0.1,
    featurePointColor: '#ee0000'
};
function displaySettingsModal(){
    var settingsModalDialog = $.dialog({
        title : '<i class="icon-wrench" style="color: #138496; font-size: 1.2em;"></i>Settings',
        content : '<settings-window></settings-window>',
        escapeKey: true,
        backgroundDismiss: true,
        useBootstrap : false,
        boxWidth : 470,
        onContentReady: function(){
            riot.mount('settings-window');
        }
    })
	window.closeSettingsModalDialog = function(){
		settingsModalDialog.close();
		settingsModalDialog = null;
		window.closeSettingsModalDialog=null;
	}
}

function displayProjectModal(){
    var projectModalDialog = $.dialog({
        title : '<i class="icon-wrench" style="color: #138496; font-size: 1.2em;"></i>Project',
        content : '<coco-open-project></coco-open-project>',
        escapeKey: true,
        backgroundDismiss: true,
        useBootstrap : false,
        boxWidth : 470,
        onContentReady: function(){
            riot.mount('coco-open-project');
        }
    })
	window.closeProjectModalDialog = function(){
		projectModalDialog.close();
		projectModalDialog = null;
		window.closeProjectModalDialog=null;
	}
}