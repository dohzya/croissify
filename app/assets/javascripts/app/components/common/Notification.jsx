import React from 'react'
import ReactDOM from 'react-dom'
import R from 'ramda'
import NotificationSystem from 'react-notification-system'

export default React.createClass({
  _notificationSystem: null,

  notify(title, message, level, position, duration) {
    if(!(title && message && level)) return
    const options = {
      'title': title,
      'message': message,
      'level': level,
      'position': position,
      'duration': duration
    }
    this._notificationSystem.addNotification(options)
  },

  componentDidMount() {
    this._notificationSystem = this.refs.notificationSystem;
  },

  style() {
    return {}
  },

  render() {
    return (
      <NotificationSystem ref="notificationSystem" style={this.style()} />
    )
  }
})
