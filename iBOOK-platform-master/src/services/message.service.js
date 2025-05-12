import axios from 'axios';
import log from '../utils/log';
import store from '../store';
import { API_URL } from '../utils/config';

export async function getMyProfile() {
  return new Promise((resolve, reject) => {
    const authHeader = {
      Authorization: `Bearer ${store.state.user.jwt_token}`,
    };
    axios
      .get(`${API_URL}/profile/me`, {
        headers: authHeader,
      })
      .then((resp) => {
        log.info(resp);
        resolve(resp.data);
      })
      .catch((error) => reject(error));
  });
}

export async function createMessage(title, content) {
  return new Promise((resolve, reject) => {
    const authHeader = {
      Authorization: `Bearer ${store.state.user.jwt_token}`,
    };
    axios
      .post(`${API_URL}/message`, { title, content }, { headers: authHeader })
      .then((resp) => {
        log.info(resp);
        resolve(resp.data);
      })
      .catch((error) => reject(error));
  });
}

export async function hideMessage(messageId, visible) {
  return new Promise((resolve, reject) => {
    const authHeader = {
      Authorization: `Bearer ${store.state.user.jwt_token}`,
    };
    axios
      // .post(`${API_URL}/message/hide/${messageId}`, { visible }, { headers: authHeader })
      .put(`${API_URL}/message/${messageId}?visible=${visible}`, '', { headers: authHeader })
      .then((resp) => {
        log.info(resp);
        resolve(resp.data);
      })
      .catch((error) => reject(error));
  });
}

export async function getMessageById(messageId) {
  return new Promise((resolve, reject) => {
    const authHeader = {
      Authorization: `Bearer ${store.state.user.jwt_token}`,
    };
    axios
      .get(`${API_URL}/message/${messageId}`, {
        headers: authHeader,
      })
      .then((resp) => {
        log.info(resp);
        resolve(resp.data);
      })
      .catch((error) => reject(error));
  });
}

export async function addReply(messageId, content) {
  return new Promise((resolve, reject) => {
    const authHeader = {
      Authorization: `Bearer ${store.state.user.jwt_token}`,
    };
    axios
      .post(`${API_URL}/message/${messageId}`, { content }, { headers: authHeader })
      .then((resp) => {
        log.info(resp);
        resolve(resp.data);
      })
      .catch((error) => reject(error));
  });
}
