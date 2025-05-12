import axios from 'axios';
import log from '../utils/log';
import store from '../store';
import { API_URL } from '../utils/config';

export async function getMyReservation() {
  return new Promise((resolve, reject) => {
    const authHeader = {
      accessToken: `${store.state.user.jwt_token}`,
    };
    axios
      .get(`${API_URL}/booking/me`, {
        headers: authHeader,
      })
      .then((resp) => {
        log.info(resp);
        resolve(resp.data);
      })
      .catch((error) => reject(error));
  });
}

export async function signIn(seatId) {
  return new Promise((resolve, reject) => {
    const authHeader = {
      accessToken: `${store.state.user.jwt_token}`,
    };
    axios
      .post(`${API_URL}/booking/signin`, { id: seatId }, { headers: authHeader })
      .then((resp) => {
        log.info(resp);
        resolve(resp.data);
      })
      .catch((error) => reject(error));
  });
}

export async function signOut(seatId) {
  return new Promise((resolve, reject) => {
    const authHeader = {
      accessToken: `${store.state.user.jwt_token}`,
    };
    axios
      .post(`${API_URL}/booking/signout`, { id: seatId }, { headers: authHeader })
      .then((resp) => {
        log.info(resp);
        resolve(resp.data);
      })
      .catch((error) => reject(error));
  });
}

export async function cancel(seatId) {
  return new Promise((resolve, reject) => {
    const authHeader = {
      accessToken: `${store.state.user.jwt_token}`,
    };
    axios
      .post(`${API_URL}/booking/cancel`, { id: seatId }, { headers: authHeader })
      .then((resp) => {
        log.info(resp);
        resolve(resp.data);
      })
      .catch((error) => reject(error));
  });
}
