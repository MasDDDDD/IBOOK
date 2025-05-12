<template>
  <v-carousel style="position: fixed; bottom: 0%; height: 100%; top: 20%;">
    <v-carousel-item
      v-for="(item,i) in items"
      :key="i"
      :src="item.src"
      reverse-transition="fade-transition"
      transition="fade-transition"
    />
  </v-carousel>
</template>

<script>
// import P1 from '../../assets/1.jpg';
// import P2 from '../../assets/2.jpg';
// import P3 from '../../assets/3.jpg';
// import P4 from '../../assets/4.jpg';

export default {
  data() {
    return {
      items: [
        // {
        //   src: P1,
        // },
        // {
        //   src: P2,
        // },
        // {
        //   src: P3,
        // },
        // {
        //   src: P4,
        // },
      ],
    };
  },
  created() {
    this.$store.commit('SET_BAR_TITLE', '欢迎访问');
  },
};
</script>