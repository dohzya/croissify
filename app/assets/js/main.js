// import pikaday from 'pikaday'
// import moment from 'moment'
// import R from 'ramda'

export function test() {
  console.log('test')
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
