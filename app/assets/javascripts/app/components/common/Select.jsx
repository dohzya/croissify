import React from 'react';
import ReactDOM from 'react-dom';
import R from 'ramda';
import onClickOutside from 'react-onclickoutside';

const initialState = () => {
  return {
    isOpen: false,
    value: null,
    children: []
  };
};
export default onClickOutside(React.createClass({

  propTypes: {
    placeholder: React.PropTypes.string,
    onChange: React.PropTypes.func.isRequired,
    selectFirst: React.PropTypes.bool,
    defaultValue: React.PropTypes.any
  },

  getInitialState: initialState,

  componentWillMount() {
    const children = React.Children.toArray(this.props.children);
    this.setState({'children': children, 'value': this.props.defaultValue});
    this.preselectValue(children);
  },

  componentWillReceiveProps(nextProps) {
    if(nextProps.children.length !== this.props.children.length && nextProps.selectFirst && !nextProps.defaultValue) {
      const children = React.Children.toArray(nextProps.children);
      this.setState({'children': children});
      this.preselectValue(children);
    }
  },

  preselectValue(children) {
    if(this.props.selectFirst && !this.props.defaultValue) {
      const first = R.head(children);
      if(first) this.selectValue(first.props.value);
    }
  },

  findLabel(value) {
    const child = this.state.children.find((c) => c.props.value === value);
    return child ? child.props.children : '';
  },
  //Required with onClickOutside wrapper
  handleClickOutside() {
    this.hide();
  },

  handleSelect(value, event) {
    event.preventDefault();
    event.stopPropagation();
    this.selectValue(value);
    this.hide();
  },

  selectValue(value) {
    this.setState({
      'value': value
    });
    this.props.onChange(value);
  },

  renderCustomOption() {
    return React.Children.map(this.props.children, (child, index) => {
      return <li key={index} onClick={this.handleSelect.bind(null, child.props.value)}>{child.props.children}</li>;
    });
  },

  display() {
    this.setState({'isOpen': true});
  },

  hide() {
    this.setState({'isOpen': false});
  },

  toggle() {
    if(this.state.isOpen) this.hide();
    else this.display();
  },

  label() {
    return this.findLabel(this.state.value);
  },

  render () {
    return (
      <div className="select">
        <select ref="select" value={this.state.value || ''} readOnly>
          {this.props.children}
        </select>
        <div className="select-custom" onClick={this.toggle}>
          <div className="selected-item">
            <span className="value">
              {this.label() || this.props.placeholder || 'Sélectionnez'}
            </span>
            <img src="/assets/img/arrow-down.svg"/>
          </div>
          <ul className={this.state.isOpen ? 'active' : ''}>
            {this.renderCustomOption()}
          </ul>
        </div>
      </div>
    );
  }
}));

