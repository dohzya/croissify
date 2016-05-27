import React from 'react'
import ReactDOM from 'react-dom'
import R from 'ramda'
import onClickOutside from 'react-onclickoutside'

import { Typeahead }  from 'react-typeahead'

const DEFAULT_NB_ENTRIES = 8

/** ENTRY MODEL
/*  Object which contains at least the key 'label' !
*/
function debounce(func, wait, immediate) {
  let timeout

  return function() {
    var context = this, args = arguments
    const later = function() {
      timeout = null
      if (!immediate) func.apply(context, args)
    }
    const callNow = immediate && !timeout
    clearTimeout(timeout)
    timeout = setTimeout(later, wait)
    if (callNow) func.apply(context, args)
  }
}

export const TypeaheadEntry = (value, label, additionalInfos) => {
  return R.merge({ 'label': label, 'value': value}, additionalInfos || {})
}


const initialState = () => {
  return {
    isOpen: false
  }
}

export let Autocomplete = onClickOutside(React.createClass({

  //Required with onClickOutside wrapper
  handleClickOutside: function(event) {
    this.hide()
  },

  componentWillMount: function() {
   this.onChange = debounce(this.props.onChange, this.props.delay || 100)
  },

  getInitialState: initialState,

  propTypes: {
    entries: React.PropTypes.array.isRequired,
    onChange: React.PropTypes.func.isRequired,
    onSelect: React.PropTypes.func.isRequired,
    placeholder: React.PropTypes.string,
    delay: React.PropTypes.number,
    className: React.PropTypes.string
  },

  onChange(value) {
    this.props.onChange(event.target.value)
  },

  onKeyUp(event) {
    event.preventDefault()

    // check if not special key do this
    let enter = event.keyCode === 13
    let arrowUp = event.keyCode === 38
    let arrowDown = event.keyCode === 40

    if(!(enter || arrowUp || arrowDown)) this.onChange(event.target.value)
  },

  handleSelect(entry, event) {
    this.props.onSelect(entry)
    this.hide()
  },

  customClasses() {
    return {
      input: "typeahead-input",
      results: "typeahead-results",
      listItem: "typeahead-list-item",
      listAnchor: "typeahead-list-anchor",
      hover: "typeahead-hover"
    }
  },

  filterOption(inputValue, entry) {
    return entry
  },

  displayOption(entry, index) {
    return entry.label
  },

  display() {
    this.setState({'isOpen': true})
  },

  hide() {
    this.setState({'isOpen': false})
  },

  className() {
    return (
      [(this.state.isOpen ? 'active ' : null), "autocomplete", this.props.className || null]
      .filter(c => c !== null)
      .join(' ')
    )
  },

  focus() {
    this.refs.typeahead.focus()
  },

  render() {
    return (
      <div className={this.className()}>
        <Typeahead
          ref="typeahead"
          onFocus={this.display}
          customClasses={this.customClasses()}
          options={this.props.entries}
          onOptionSelected={this.handleSelect}
          filterOption={this.filterOption}
          displayOption={this.displayOption}
          onKeyUp={this.onKeyUp}
          maxVisible={this.props.maxVisible || DEFAULT_NB_ENTRIES}
          placeholder={this.props.placeholder || 'type here'} />
      </div>
    )
  }
}))
