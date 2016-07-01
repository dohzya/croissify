import 'whatwg-fetch';
import R from 'ramda';

const Method = {GET: 'GET', POST: 'POST', PUT: 'PUT', DELETE: 'DELETE'};
const BaseUri = () => window.location.hostname;

async function checkStatus(response) {
  if (response.ok) {
    return response;
  } else {
    var error = new Error(response.statusText);
    error.response = response;
    error.status = response.status;
    error.message = await response.text();
    throw error;
  }
}

function parseJSON(response) {
  return new Promise((resolve) => {
    response.json()
      .then(res => {
        if (res) resolve(res);
        else resolve();
      })
      .catch(() => {
        resolve();
      });
  });
}

function asyncRequest(method, url, params) {
  const options = {
    method: Method.GET,
    credentials: 'include'
  };
  let p;
  switch (method) {
    case Method.GET : {
      p = fetch(url, options);
      break;
    }
    case Method.POST : {
      let postOptions = R.merge(options, {method: Method.POST, body: JSON.stringify(params)});
      p = fetch(url, postOptions);
      break;
    }
  }
  return p.then(checkStatus).then(parseJSON);
}

export const Communication = {
  test() {
    const url = BaseUri() + '/test';
    return asyncRequest(Method.GET, url);
  }
};
