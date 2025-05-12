<template>
  <v-form>
    <v-container fluid>
      <v-row justify="center">
        <v-col cols="12" sm="8" md="6" lg="4">
          <v-text-field
            v-model="name"
            :rules="[rules.required, rules.min, rules.max]"
            label="姓名"
            hint="阁下怎么称呼？"
          />
        </v-col>
      </v-row>
      <v-row justify="center">
        <v-col cols="12" sm="8" md="6" lg="4">
          <v-text-field
            v-model="stuNum"
            :rules="[rules.required, rules.min, rules.max]"
            label="学号"
            hint="请输入学号"
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
            hint="建议使用复杂密码"
            counter
            @click:append="showPassword = !showPassword"
          />
        </v-col>
      </v-row>
      <v-row justify="center">
        <v-col cols="12" sm="8" md="6" lg="4">
          <v-text-field
            v-model="reEnter"
            :append-icon="showReEnter ? 'mdi-eye' : 'mdi-eye-off'"
            :rules="[rules.required, rules.max]"
            :type="showReEnter ? 'text' : 'password'"
            label="再输一遍密码"
            hint="same with last one"
            counter
            @click:append="showReEnter = !showReEnter"
          />
        </v-col>
      </v-row>
      <v-row justify="center">
        <v-btn
          color="primary"
          large
          dark
          @click="register"
        >
          给我也整一个
        </v-btn>
      </v-row>
      <v-snackbar v-model="snackbar" :color="snackbarColor" :timeout="snackbarTimeout">
        {{ snackbarText }}
        <v-btn dark text @click="snackbar = false">
          Close
        </v-btn>
      </v-snackbar>
    </v-container>
  </v-form>
</template>

<script>
import { register } from '../../services/auth.service';
import log from '../../utils/log';

export default {
  data() {
    return {
      showPassword: false,
      showReEnter: false,
      name: '',
      stuNum: '',
      password: '',
      reEnter: '',

      rules: {
        required: (value) => !!value || 'Required.',
        min: (v) => v.length >= 4 || 'Min 4 characters',
        max: (v) => v.length <= 24 || 'Max 24 characters',
      },
      snackbar: '',
      snackbarColor: '',
      snackbarText: '',
      snackbarTimeout: 2000,
    };
  },
  computed: {
    disableRegister() {
      const name = this.name !== null && this.name.length >= 4 && this.name.length <= 24;
      const stuNum = this.stuNum !== null && this.stuNum.length >= 4 && this.stuNum.length <= 24;
      const psw = this.password !== null && this.password.length >= 4 && this.password.length <= 24;
      const repsw = this.reEnter !== null && this.reEnter.length <= 24;
      if (this.reEnter === this.password && name && stuNum && psw && repsw) {
        return false;
      }
      return true;
    },
  },
  created() {
    this.$store.commit('SET_BAR_TITLE', '注册');
  },
  methods: {
    showSnackbar(type, content) {
      this.snackbarColor = type;
      this.snackbarText = content;
      this.snackbar = true;
    },
    register() {
      if (this.disableRegister) {
        this.showSnackbar('warning', '输入格式有误');
        return;
      }
      register(this.name, this.stuNum, this.password)
        .then((resp) => {
          if (resp.data.message === 'register successfully!') {
            this.showSnackbar('success', '注册成功，跳转登录页面……');
            setTimeout(() => {
              this.$router.push('login');
            }, 1000);
          }
        })
        .catch((e) => {
          log.info(`error: ${e}`);
          this.showSnackbar('error', '注册失败');
        });
    },
  },
};
</script>
