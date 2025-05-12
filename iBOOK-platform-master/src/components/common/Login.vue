<template>
  <v-form>
    <v-container fluid>
      <v-row justify="center">
        <v-col cols="12" sm="8" md="6" lg="4">
          <v-text-field
            v-model="stuNum"
            :rules="[rules.required, rules.min, rules.max]"
            label="学号"
          />
        </v-col>
      </v-row>
      <v-row justify="center">
        <v-col cols="12" sm="8" md="6" lg="4">
          <v-text-field
            v-model="password"
            :append-icon="showPassword ? 'mdi-eye' : 'mdi-eye-off'"
            :rules="[rules.required, rules.min, rules.max]"
            :type="showPassword ? 'text' : 'password'"
            label="密码"
            @click:append="showPassword = !showPassword"
          />
        </v-col>
      </v-row>
      <v-snackbar v-model="snackbar" :color="snackbarColor" :timeout="snackbarTimeout">
        {{ snackbarText }}
        <v-btn dark text @click="snackbar = false">
          Close
        </v-btn>
      </v-snackbar>
      <v-row justify="center">
        <v-col cols="12" sm="8" md="6" lg="4">
          <v-row justify="center">
            <v-btn color="primary" large dark @click="login">
              登录
            </v-btn>
          </v-row>
        </v-col>
      </v-row>
      <v-btn
        class="mx-2"
        style="position: fixed; bottom: 10%; right: 5%;"
        fab
        dark
        color="indigo"
        @click="$router.push('/register')"
      >
        <v-icon dark>
          mdi-plus
        </v-icon>
      </v-btn>
    </v-container>
  </v-form>
</template>

<script>
import { mapGetters, mapState } from 'vuex';
import { login } from '../../services/auth.service';

import { getMyReservation } from '../../services/reservation.service';
import { getRooms } from '../../services/room.service';
import log from '../../utils/log';

export default {
  data: () => ({
    showPassword: false,
    stuNum: '',
    password: '',
    rules: {
      required: (value) => !!value || 'Required.',
      min: (v) => v.length >= 3 || 'Min 3 characters',
      max: (v) => v.length <= 24 || 'Max 24 characters',
      // validEmail: (v) => /.+@.+/.test(v) || 'E-mail must be valid',
    },
    snackbar: '',
    snackbarColor: '',
    snackbarText: '',
    snackbarTimeout: 2000,
  }),
  created() {
    this.$store.commit('SET_BAR_TITLE', '登录');
  },
  methods: {
    disableLogin() {
      const stuNum = this.stuNum !== null && this.stuNum.length >= 3 && this.stuNum.length <= 24;
      const psw = this.password !== null && this.password.length >= 3 && this.password.length <= 24;
      if (stuNum && psw) {
        return false;
      }
      return true;
    },
    showSnackbar(type, content) {
      this.snackbarColor = type;
      this.snackbarText = content;
      this.snackbar = true;
    },
    getProfile() {
      getMyProfile()
        .then((profile) => {
          log.info('front:', profile);
          // this.$store.commit('SET_MESSAGES', messages);
        })
        .catch((err) => {
          log.info(err);
        });
    },

    fetchData() {
      getRooms()
        .then((rooms) => {
          this.$store.commit('SET_ROOMS', rooms);
          log.info(this.rooms);
        })
        .catch((err) => {
          log.info(err);
        });
      getMyReservation()
        .then((reservations) => {
          this.$store.commit('SET_RESERVATIONS', reservations);
          log.info(this.reservations);
        })
        .catch((err) => {
          log.info(err);
        });
    },

    login() {
      if (this.disableLogin()) {
        this.showSnackbar('warning', '输入格式错误');
        return;
      }
      log.info(this.$router.url);
      login(this.stuNum, this.password)
        .then((resp) => {
          const jwtToken = resp.data.accessToken;
          log.info('front: ', jwtToken);
          const { name } = resp.data.user;
          const usr_id = resp.data.user.id;
          const { stuNum } = resp.data.user;
          const { identity } = resp.data;

          this.$store.commit('SET_JWT_TOKEN', jwtToken);

          this.fetchData();

          this.$store.commit('SET_USER', { id: usr_id, name, stuNum, identity });

          this.showSnackbar('success', '登录成功');
          const { redirect } = this.$router.currentRoute.query;
          const redirect1 = 'room';
          const redirect2 = 'admin/room';
          if (redirect) {
            log.info('redirect to: ', redirect);
            this.$router.push(redirect);
          } else if (identity === 'admin') {
            log.info('redirect to: ', redirect2);
            this.$router.push(redirect2);
          } else {
            log.info('redirect to: ', redirect1);
            this.$router.push(redirect1);
          }
        })
        .catch((e) => {
          log.info(e);
          log.info(`error: ${e}`);
          this.showSnackbar('error', '登录失败');
        });
    },
  },
};
</script>
