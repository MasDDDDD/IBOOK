import Vue from 'vue';
import VueRouter from 'vue-router';
import store from '../store';
import Temp from '../components/common/Temp.vue';
import Welcome from '../components/common/Welcome.vue';
import Login from '../components/common/Login.vue';
import Register from '../components/common/Register.vue';
import Reservation from '../components/reservation/Reservation.vue';
import RoomTable from '../components/room/RoomTable.vue';
import ManageRooms from '../components/room/ManageRooms.vue';
import ManageSeat from '../components/seat/ManageSeat.vue';
import RoomInfo from '../components/room/RoomInfo.vue';
import SeatTable from '../components/seat/SeatTable.vue';
import UserInfo from '../components/user/UserInfo.vue';
import ManageUsers from '../components/user/ManageUsers.vue';

Vue.use(VueRouter);

const routes = [
  // {
  //   path: '/',
  //   name: 'Welcome',
  //   component: Welcome,
  // },
  {
    path: '/temp',
    name: 'Temp',
    component: Temp,
  },
  {
    path: '/login',
    name: 'Login',
    component: Login,
  },
  {
    path: '/register',
    name: 'Register',
    component: Register,
  },
  {
    path: '/reservation',
    component: Reservation,
    meta: { requireAuth: true },
    props: true,
  },
  {
    path: '/room',
    component: RoomTable,
    meta: { requireAuth: false },
    props: true,
  },
  {
    path: '/admin/room',
    name: 'ManageRooms',
    component: ManageRooms,
    meta: { requireAuth: true },
    props: true,
  },
  {
    path: '/admin/room/:id',
    component: ManageSeat,
    meta: { requireAuth: false },
    props: true,
  },
  {
    path: '/room/:id',
    component: RoomInfo,
    meta: { requireAuth: false },
    props: true,
  },
  {
    path: '/seat',
    component: SeatTable,
    meta: { requireAuth: true },
    props: true,
  },
  {
    path: '/userinfo',
    component: UserInfo,
    meta: { requireAuth: true },
    props: true,
  },
  {
    path: '/admin/user',
    component: ManageUsers,
    meta: { requireAuth: true },
    props: true,
  },
  { path: '*', component: Login },
];

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes,
});

router.beforeEach((to, from, next) => {
  if (to.matched.some((record) => record.meta.requireAuth)) {
    // 如果需要登录才可见
    if (store.getters.userLoggedIn) {
      next();
    } else {
      next({ name: 'Login', query: { redirect: to.fullPath } });
    }
  } else {
    next();
  }
});

export default router;
