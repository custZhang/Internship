<template>
  <div>
    <svg-icon icon-class="github" @click="goto"/>
  </div>
</template>

<script>
export default {
  name: 'internshipGit',
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
