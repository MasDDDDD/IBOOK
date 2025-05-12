import axios from 'axios';
import log from '../utils/log';
import store from '../store';

import { API_URL } from '../utils/config';

export async function getSeatsByRoomId(roomId) {
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

export async function addSeat(studyRoomId) {
  return new Promise((resolve, reject) => {
    const authHeader = {
      accessToken: `${store.state.user.jwt_token}`,
    };
    axios
      .post(`${API_URL}/admin/seat`, { studyRoomId }, { headers: authHeader })
      .then((resp) => {
        log.info(resp);
        resolve(resp.data);
      })
      .catch((error) => reject(error));
  });
}

export async function deleteSeat(seatId) {
  return new Promise((resolve, reject) => {
    const authHeader = {
      accessToken: `${store.state.user.jwt_token}`,
    };
    axios
      .delete(`${API_URL}/admin/seat`, { data: { id: seatId }, headers: authHeader })
      .then((resp) => {
        log.info(resp);
        resolve(resp.data);
      })
      .catch((error) => reject(error));
  });
}
