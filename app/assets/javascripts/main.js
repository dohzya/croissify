import 'babel-polyfill';

import React from 'react';
import ReactDOM from 'react-dom';
import { notify } from './app/modules/Notification';
import Notification from './app/components/common/Notification.jsx';

window.React = React;
window.ReactDOM = ReactDOM;

window.UI = {};
window.notify = notify;

document.addEventListener('DOMContentLoaded', () => {
  window.UI.Notification = ReactDOM.render(React.createElement(Notification), document.getElementById('notification-parent'));
});
