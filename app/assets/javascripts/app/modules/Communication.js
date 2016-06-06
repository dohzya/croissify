import 'whatwg-fetch'
import R from 'ramda'

const Method = { GET: 'GET', POST: 'POST', PUT: 'PUT', DELETE: 'DELETE' }
const BaseUri = () => window.location.hostname

async function checkStatus(response) {
  if (response.ok) {
    return response
  } else {
    var error = new Error(response.statusText)
    error.response = response
    error.status = response.status
    error.message = await response.text()
    throw error
  }
}

function parseJSON(response) {
  return new Promise((resolve) => {
    response.json()
      .then((res) => {
        if(res) resolve(res)
        else resolve()
      })
      .catch(() => {
        resolve()
      })
  })
}

function buildQSUrl(url, params) {
  let queryString = '';
  if ( params ) {
    let buildQueryString = (query, key) => [query, key, '=', params[key],'&'].join('');
    queryString = R.reduce(buildQueryString, '?', R.keys(params)).slice(0,-1);
  }
  return url + queryString;
}

function asyncRequest(method, url, params, contentType) {
  const options = {
    method: Method.GET,
    credentials: 'include'
  }
  let p;
  switch (method) {
    case Method.GET :
      p = fetch(url, options)
      break;

    case Method.POST :
      let postOptions = R.merge(options, {method: Method.POST, body: JSON.stringify(params)})
      p = fetch(url, postOptions)
      break;
  }
  return p.then(checkStatus).then(parseJSON)
}

export const Communication = {
  test(params) {
    const url = BaseUri() + '/test'
    return asyncRequest(Method.GET, url)
  }
}
