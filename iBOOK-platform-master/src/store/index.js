import Vue from 'vue';
import Vuex from 'vuex';
import createPersistedState from 'vuex-persistedstate';
import log from '../utils/log';

Vue.use(Vuex);

export default new Vuex.Store({
  state: {
    user: {
      id: '',
      name: '',
      stuNum: '',
      jwt_token: '',
      identity: '',
    },
    reservations: [],
    rooms: [],
    seats: [],
    books: [],
    barTitle: 'Welcome!',
  },
  mutations: {
    SET_BAR_TITLE(state, barTitle) {
      state.barTitle = barTitle;
    },
    SET_USER(state, user) {
      state.user.id = user.id;
      state.user.name = user.name;
      state.user.stuNum = user.stuNum;
      state.user.identity = user.identity;
    },
    SET_JWT_TOKEN(state, token) {
      state.user.jwt_token = token;
    },
    SET_RESERVATIONS(state, reservations) {
      state.reservations = reservations;
    },
    SET_ROOMS(state, rooms) {
      state.rooms = rooms;
    },
    SET_SEATS(state, seats) {
      state.seats = seats;
    },
    SET_BOOKS(state, books) {
      state.books = books;
    },
    // changeMessageVisible(state, messageId, visible) {
    //   for (let i = 0; i < state.messages.length; i += 1) {
    //     if (state.messages[i].id === messageId) {
    //       state.messages[i].visible = visible;
    //     }
    //   }
    // },
    LOGOUT(state) {
      log.info('LOGOUT');
      state.user.jwt_token = '';
      state.user.name = '未登录';
      state.user.id = '';
      state.user.stuNum = '';
      state.user.identity = 'student';
    },
  },
  getters: {
    userLoggedIn: (state) => state.user.jwt_token !== '',
    countReservations: (state) => Object.keys(state.reservations).length,
    countRooms: (state) => Object.keys(state.rooms).length,
  },
  plugins: [
    createPersistedState({
      storage: window.sessionStorage,
      reducer(state) {
        return {
          user: state.user,
          messages: state.messages,
          reservations: state.reservations,
        };
      },
    }),
  ],
  actions: {},
  modules: {},
});
