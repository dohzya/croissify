import d from '../libs/date';

const DEFAULT_EXPIRATION = 60 * 60 * 1000;

export const Cache = {

  get(key) {
    let item = JSON.parse(window.sessionStorage.getItem(key));
    return item && Date.now() < item.expirationDate ? item.value : null;
  },

  set(key, value, expirationMillis) {
    let expDate = d.nowWithExpiration(expirationMillis || DEFAULT_EXPIRATION);
    let item = JSON.stringify({expirationDate: expDate, value: value});
    window.sessionStorage.setItem(key, item);
  },

  remove(key) {
    window.sessionStorage.removeItem(key);
  }
};
