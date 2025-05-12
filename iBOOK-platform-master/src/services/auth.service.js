import axios from 'axios';
import log from '../utils/log';
import $store from '../store';
// import store from '../store';

import { API_URL } from '../utils/config';

export function isAccessTokenExpired() {
  const accessTokenExpDate = $store.state.auth.accessTokenExpDate - 1;
  const nowTime = Math.floor(new Date().getTime() / 1000);

  return accessTokenExpDate <= nowTime;
}

export function getRefreshToken() {
  return localStorage.getItem('refreshToken');
}

export function getAccessToken() {
  return $store.state.user.token;
  // return localStorage.getItem('accessToken');
}

export function login(username, password) {
  return new Promise((resolve, reject) => {
    const data = {
      stuNum: username,
      password,
    };
    log.info('login payload: ', data);
    axios
      .post(`${API_URL}/login`, data)
      .then((resp) => {
        log.info('login backend resp:', resp);
        resolve(resp);
      })
      .catch((error) => reject(error));
  });
}

export function register(name, stuNum, password) {
  return new Promise((resolve, reject) => {
    const payload = {
      name,
      stuNum,
      password,
    };
    log.info('register payload: ', payload);
    axios
      .post(`${API_URL}/register`, payload)
      .then((resp) => {
        log.info('register resp Backend', resp);
        resolve(resp);
      })
      .catch((err) => reject(err));
  });
}

export async function getMyProfile() {
  return new Promise((resolve, reject) => {
    const authHeader = {
      accessToken: `${$store.state.user.jwt_token}`,
    };
    axios
      .get(`${API_URL}/profile/me`, {
        headers: authHeader,
      })
      .then((resp) => {
        log.info(resp);
        resolve(resp);
      })
      .catch((error) => reject(error));
  });
}
