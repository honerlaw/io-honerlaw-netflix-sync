
import $ from "jquery";
import { click, move, clickPoint, bind } from "./EventDispatcher";
import { toggleVideo, seekVideo, tickVideo, joinSession } from "./Request";
import Store from "./Store";

export function init() {

    if(window.localStorage.getItem('session') !== null) {
        window.localStorage.removeItem('session');
        joinSession(window.localStorage.getItem('session'));
    }

    Store.subscribe(() => {
        let playing = Store.getState().playing;
        let seconds = Store.getState().seconds;
        let url = Store.getState().url;
        if(url !== null && url !== window.location.href) {
            if(Store.getState().session !== null) {
                window.localStorage.setItem('session', Store.getState().session.id);
            }
            window.location.href = url;
        }
        /*if(playing !== null) {
            if(playing === true) {
                play();
            } else if(playing === false) {
                pause();
            }
        }*/
        if(seconds !== null) {
            seek(seconds, 3);
        }
    });

    // handle events
    bind((e) => {

        // if not in a session
        if(Store.getState().session === null) {
            // make sure we restore the scrubber
            if($('.player-scrubber-target').length > 0) {
                if($('.player-scrubber-target').css('display') !== 'block') {
                    $('.player-scrubber-target').css({ display : 'block' });
                }
            }
            return;
        }

        // remove the scrubber drag button
        if($('.player-scrubber-target').length > 0) {
            if($('.player-scrubber-target').css('display') !== 'none') {
                $('.player-scrubber-target').css({ display : 'none' });
            }
        }

        // get the element we clicked on
        let ele = $(e.target);

        // check if it was a click event or keyup event
        if(e.type === "click") {
            // user clicked on the play / pause buttom
            if(ele.hasClass("player-play-pause")) {
                toggleVideo($('.player-play-pause').hasClass("play"));
            }
            // user clicked somewhere on the seek bar to try and seek to a new location
            if(ele.hasClass('player-scrubber') || ele.closest('.player-scrubber').length > 0) {
                seekRequest();
            }
        } else if(e.type === "keyup") {
            // get the keycode of the key that was clicked
            let keyCode = e.keyCode || e.which;
            // if it is the space bar we are trying to pause / play the video
            if(keyCode === 32) {
                toggleVideo($('.player-play-pause').hasClass("play"));
            }
        }
    });

    // update video data
    setInterval(() => {
        let video = $('.player-video-wrapper video')[0];
        if(video !== undefined) {
            tickVideo(window.location.href, !$('.player-play-pause').hasClass('play'), Math.round(video.currentTime));
        }
    }, 1500);
}

/**
 * Send out the request to seek when a user clicks (versus drags)
 */
function seekRequest() {
    // get the video player element
    let video = $('.player-video-wrapper video')[0];

    // get the current time the player is at (before we seek)
    let currentTime = Math.round(video.currentTime);

    // holds the seek time position (what we are trying to go to)
    let seconds;

    // calculate the seek time position
    let pieces = $('.trickplay-preview time').html().split(":");
    if(pieces.length === 2) {
        seconds = (parseInt(pieces[0]) * 60) + parseInt(pieces[1]);
    } else if(pieces.length === 3) {
        seconds = (parseInt(pieces[0]) * 3600) + (parseInt(pieces[1]) * 60) + parseInt(pieces[2]);
    }

    // send out the request
    seekVideo(seconds);
}

/**
 * Seek to the given number of seconds
 */
function seek(seconds, threshold) {

    // the threshold on whether or not to try and update the position
    if(threshold === undefined) {
        threshold = 0;
    }

    // get the video player element
    let video = $('.player-video-wrapper video');
    if(video.length === 0) {
        return;
    }
    video = video[0];

    // get the current video time
    let currentTime = Math.round(video.currentTime);

    // we are in the threshold so do not do anything
    if(seconds <= currentTime + threshold && seconds >= currentTime - threshold) {
        return;
    }

    // the duration of the video
    let duration = video.duration;

    // make sure we are trying to seek to a valid time
    if(seconds >= 0 && seconds <= duration) {

        // calculate the percentage through the video we are
        let percent = (seconds / duration);

        // dispatch move event to cause slider to be displayed
        move('#scrubber-component');

        // wait for the scrubber to appear
        setTimeout(function() {
            let scrubber = $('#scrubber-component');

            // make sure we are interacting with the horizontal scrubber
            if(scrubber.hasClass('horizontal')) {

                // get the progress bar we want to click on
                let progress = scrubber.find('.player-scrubber-progress');

                // get the progress bar's offset so we can calculate where to click
                let offset = progress.offset();
                let topOffset = Math.round(offset.top);
                let leftOffset = Math.round(offset.left);

                // get the width of the progress bar so we can calculate where to click
                let width = Math.round(progress.width());

                // get the progress bar's height to calculaate where to click
                let height = Math.round(progress.height());

                // calculate the offset in the progress bar of where we need to calculate
                let progressLeftOffset = Math.round(width * percent);

                // get the mouse click coordinates
                let clickX = progressLeftOffset + leftOffset;
                let clickY = topOffset + Math.round(height / 2);

                // click the point we calculated above
                clickPoint(scrubber, clickX, clickY);
            }
        }.bind(this), 10);

    }
}

/**
 * Checks if the video is paused and if it is tries to start playing
 */
function play() {
    let ele = $('.player-play-pause');
    if(ele.hasClass('play')) {
        click(ele);
    }
}

/**
 * Checks if the video is playing and if it is tries to pause it
 */
function pause() {
    let ele = $('.player-play-pause');
    if(!ele.hasClass('play')) {
        click(ele);
    }
}
