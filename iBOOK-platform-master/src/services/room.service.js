import axios from 'axios';
import log from '../utils/log';
import store from '../store';

import { API_URL } from '../utils/config';

export async function getRooms() {
  return new Promise((resolve, reject) => {
    const authHeader = {
      accessToken: `${store.state.user.jwt_token}`,
    };
    axios
      .get(`${API_URL}/studyroom`, {
        headers: authHeader,
      })
      .then((resp) => {
        resolve(resp.data);
      })
      .catch((error) => reject(error));
  });
}

export async function getRoomById(roomId) {
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

export async function addRoom(buildingNum, classRoomNum, startTime, endTime) {
  return new Promise((resolve, reject) => {
    const authHeader = {
      accessToken: `${store.state.user.jwt_token}`,
    };
    axios
      .post(
        `${API_URL}/admin/studyroom`,
        { buildingNum, classRoomNum, startTime, endTime },
        { headers: authHeader }
      )
      .then((resp) => {
        log.info(resp);
        resolve(resp.data);
      })
      .catch((error) => reject(error));
  });
}

export async function editRoomTime(roomId, startTime, endTime) {
  return new Promise((resolve, reject) => {
    const authHeader = {
      accessToken: `${store.state.user.jwt_token}`,
    };
    axios
      .post(
        `${API_URL}/admin/studyroom/time`,
        { id: roomId, startTime, endTime },
        { headers: authHeader }
      )
      .then((resp) => {
        log.info(resp);
        resolve(resp.data);
      })
      .catch((error) => reject(error));
  });
}

export async function editRoomState(roomId, state) {
  return new Promise((resolve, reject) => {
    const authHeader = {
      accessToken: `${store.state.user.jwt_token}`,
    };
    axios
      .post(`${API_URL}/admin/studyroom/state`, { id: roomId, state }, { headers: authHeader })
      .then((resp) => {
        log.info('admin/studyroom/state', resp);
        resolve(resp.data);
      })
      .catch((error) => reject(error));
  });
}

export async function deleteRoom(roomId) {
  return new Promise((resolve, reject) => {
    const authHeader = {
      accessToken: `${store.state.user.jwt_token}`,
    };
    axios
      .delete(`${API_URL}/admin/studyroom`, { data: { id: roomId }, headers: authHeader })
      .then((resp) => {
        log.info(resp);
        resolve(resp.data);
      })
      .catch((error) => reject(error));
  });
}
