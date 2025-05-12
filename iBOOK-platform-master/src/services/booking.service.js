import axios from 'axios';
import log from '../utils/log';
import store from '../store';

import { API_URL } from '../utils/config';

export async function getRoom(roomId) {
  return new Promise((resolve, reject) => {
    const authHeader = {
      accessToken: `${store.state.user.jwt_token}`,
    };
    axios
      .get(`${API_URL}/studyroom/${roomId}`, {
        headers: authHeader,
      })
      .then((resp) => {
        resolve(resp.data);
      })
      .catch((error) => reject(error));
  });
}

export async function getSeats(roomId) {
  return new Promise((resolve, reject) => {
    const authHeader = {
      accessToken: `${store.state.user.jwt_token}`,
    };
    axios
      .get(`${API_URL}/seat/${roomId}`, {
        headers: authHeader,
      })
      .then((resp) => {
        resolve(resp.data);
      })
      .catch((error) => reject(error));
  });
}

export async function getBooks(seatId) {
  return new Promise((resolve, reject) => {
    const authHeader = {
      accessToken: `${store.state.user.jwt_token}`,
    };
    axios
      .get(`${API_URL}/booking/seat/${seatId}`, {
        headers: authHeader,
      })
      .then((resp) => {
        resolve(resp.data);
      })
      .catch((error) => reject(error));
  });
}

export function booking(seatId, startTime, bookingPeriod) {
  return new Promise((resolve, reject) => {
    const authHeader = {
      accessToken: `${store.state.user.jwt_token}`,
    };
    const payload = {
      seatId,
      startTime,
      bookingPeriod,
    };
    axios
      .post(`${API_URL}/booking`, payload, { headers: authHeader })
      .then((resp) => {
        log.info('booking resp Backend', resp);
        resolve(resp);
      })
      .catch((err) => reject(err));
  });
}

export function getUserBooks(stuNum) {
  return new Promise((resolve, reject) => {
    const authHeader = {
      accessToken: `${store.state.user.jwt_token}`,
    };
    axios
      .get(`${API_URL}/admin/booking/${stuNum}`, { headers: authHeader })
      .then((resp) => {
        resolve(resp.data);
      })
      .catch((err) => reject(err));
  });
}
