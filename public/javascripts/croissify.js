(function webpackUniversalModuleDefinition(root, factory) {
	if(typeof exports === 'object' && typeof module === 'object')
		module.exports = factory();
	else if(typeof define === 'function' && define.amd)
		define("Croissify", [], factory);
	else if(typeof exports === 'object')
		exports["Croissify"] = factory();
	else
		root["Croissify"] = factory();
})(this, function() {
return /******/ (function(modules) { // webpackBootstrap
/******/ 	// The module cache
/******/ 	var installedModules = {};

/******/ 	// The require function
/******/ 	function __webpack_require__(moduleId) {

/******/ 		// Check if module is in cache
/******/ 		if(installedModules[moduleId])
/******/ 			return installedModules[moduleId].exports;

/******/ 		// Create a new module (and put it into the cache)
/******/ 		var module = installedModules[moduleId] = {
/******/ 			exports: {},
/******/ 			id: moduleId,
/******/ 			loaded: false
/******/ 		};

/******/ 		// Execute the module function
/******/ 		modules[moduleId].call(module.exports, module, module.exports, __webpack_require__);

/******/ 		// Flag the module as loaded
/******/ 		module.loaded = true;

/******/ 		// Return the exports of the module
/******/ 		return module.exports;
/******/ 	}


/******/ 	// expose the modules object (__webpack_modules__)
/******/ 	__webpack_require__.m = modules;

/******/ 	// expose the module cache
/******/ 	__webpack_require__.c = installedModules;

/******/ 	// __webpack_public_path__
/******/ 	__webpack_require__.p = "";

/******/ 	// Load entry module and return exports
/******/ 	return __webpack_require__(0);
/******/ })
/************************************************************************/
/******/ ([
/* 0 */
/***/ function(module, exports, __webpack_require__) {

	__webpack_require__(1);
	module.exports = __webpack_require__(2);


/***/ },
/* 1 */
/***/ function(module, exports) {

	'use strict';

	Object.defineProperty(exports, "__esModule", {
	  value: true
	});
	exports.test = test;
	// import pikaday from 'pikaday'
	// import moment from 'moment'
	// import R from 'ramda'

	function test() {
	  console.log('test');
	}

	// function Croissify() {
	//   function startPika(container, croissants) {
	//     const picker = new Pikaday({
	//       disableWeekends: true,
	//       numberOfMonths: 2,
	//       minDate: moment().add(1, 'day').toDate(),
	//       maxDate: moment().add(2, 'month').toDate(),
	//       disableDayFn: date => {
	//         console.log(date)
	//         const isDayTaken = R.find(croissant => {
	//             if(!croissant.doneDate) return false
	//             const doneDateMoment = moment(croissant.doneDate)
	//             console.log('date tested', date)
	//               console.log('doneDateMoment', doneDateMoment)
	//             console.log(moment(date).isSame(doneDateMoment, 'd'))
	//             return moment(date).isSame(doneDateMoment, 'd')
	//         })(croissants)
	//         return isDayTaken
	//       }
	//     });
	//     field.parentNode.insertBefore(picker.el, container.nextSibling);
	//   }
	// }


	// (function (document) {
	//
	//   function tryParseJson(txt) {
	//     try {
	//       return JSON.parse(txt);
	//     } catch (e) {
	//       return undefined;
	//     }
	//   }
	//
	//   function ajaxError(req) {
	//     const json = tryParseJson(req.response);
	//     return json || {error: 'error while sending HTTP request'};
	//   }
	//
	//   function ajaxPOST(url, cb) {
	//     const req = new XMLHttpRequest();
	//     req.open('POST', url, true);
	//     req.onreadystatechange = () => {
	//       if (req.readyState == 4) {
	//         if (req.status == 200) cb(undefined, tryParseJson(req.response));
	//         else cb(ajaxError(req));
	//       }
	//     };
	//     req.send(null);
	//   }
	//
	//   function runAction(action, id) {
	//     ajaxPOST('/api/actions/' + action + '?id=' + id, (err, resp) => {
	//       if (err) console.error('Error while sending action:', err.error);
	//       else {
	//         if (resp.reload === true) window.location.reload();
	//       }
	//     });
	//   }
	//
	//   function init() {
	//     const actions = document.querySelectorAll('[data-action]');
	//     for (let i=0; i < actions.length; i++) {
	//       actions[i].onclick = (e) => {
	//         const target = e.target;
	//         const action = target.getAttribute('data-action');
	//
	//         let id = undefined;
	//         {
	//           let it = target.parentElement;
	//           while (it) {
	//             id = it.getAttribute('data-id');
	//             if (id) break;
	//             it = it.parentElement;
	//           }
	//         }
	//
	//         if (action && id) runAction(action, id);
	//       };
	//     }
	//   }
	//
	//   if (document.readyState != 'loading') init();
	//   else document.addEventListener('DOMContentLoaded', init);
	//
	// })(document);

/***/ },
/* 2 */
/***/ function(module, exports) {

	// removed by extract-text-webpack-plugin

/***/ }
/******/ ])
});
;