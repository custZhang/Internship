<template>
  <div>
    <svg-icon icon-class="question" @click="goto"/>
  </div>
</template>

<script>
export default {
  name: 'internshipDoc',
  data() {
    return {
      url: 'https://javainternship.cn'
    }
  },
  methods: {
    goto() {
      window.open(this.url)
    }
  }
}
</script>
