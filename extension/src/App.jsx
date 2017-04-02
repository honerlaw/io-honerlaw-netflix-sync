
import React from "react";
import ReactDOM from "react-dom";
import $ from "jquery";
import Navigator from "./ui/Navigator";
import { init } from "./Player";

// add the mount point to the body
let mount = $('<div id="syncMountPoint"></div>');
if(window.location.href.indexOf('watch') === -1) {
    mount.css('display', 'none');
}
$('body').append(mount);

// initialize everything related to the netflix player
init();

// render the app
ReactDOM.render(<Navigator />, document.getElementById("syncMountPoint"));

// Check the url and display / hide the nav bar
setInterval(() => {
    if(window.location.href.indexOf('/watch') === -1) {
        $('#syncMountPoint').css('display', 'none');
    } else {
        $('#syncMountPoint').css('display', 'block');
    }
}, 750);
