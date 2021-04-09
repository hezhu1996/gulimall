<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="»áÔ±µÈ¼¶id" prop="levelId">
      <el-input v-model="dataForm.levelId" placeholder="»áÔ±µÈ¼¶id"></el-input>
    </el-form-item>
    <el-form-item label="ÓÃ»§Ãû" prop="username">
      <el-input v-model="dataForm.username" placeholder="ÓÃ»§Ãû"></el-input>
    </el-form-item>
    <el-form-item label="ÃÜÂë" prop="password">
      <el-input v-model="dataForm.password" placeholder="ÃÜÂë"></el-input>
    </el-form-item>
    <el-form-item label="êÇ³Æ" prop="nickname">
      <el-input v-model="dataForm.nickname" placeholder="êÇ³Æ"></el-input>
    </el-form-item>
    <el-form-item label="ÊÖ»úºÅÂë" prop="mobile">
      <el-input v-model="dataForm.mobile" placeholder="ÊÖ»úºÅÂë"></el-input>
    </el-form-item>
    <el-form-item label="ÓÊÏä" prop="email">
      <el-input v-model="dataForm.email" placeholder="ÓÊÏä"></el-input>
    </el-form-item>
    <el-form-item label="Í·Ïñ" prop="header">
      <el-input v-model="dataForm.header" placeholder="Í·Ïñ"></el-input>
    </el-form-item>
    <el-form-item label="ÐÔ±ð" prop="gender">
      <el-input v-model="dataForm.gender" placeholder="ÐÔ±ð"></el-input>
    </el-form-item>
    <el-form-item label="ÉúÈÕ" prop="birth">
      <el-input v-model="dataForm.birth" placeholder="ÉúÈÕ"></el-input>
    </el-form-item>
    <el-form-item label="ËùÔÚ³ÇÊÐ" prop="city">
      <el-input v-model="dataForm.city" placeholder="ËùÔÚ³ÇÊÐ"></el-input>
    </el-form-item>
    <el-form-item label="Ö°Òµ" prop="job">
      <el-input v-model="dataForm.job" placeholder="Ö°Òµ"></el-input>
    </el-form-item>
    <el-form-item label="¸öÐÔÇ©Ãû" prop="sign">
      <el-input v-model="dataForm.sign" placeholder="¸öÐÔÇ©Ãû"></el-input>
    </el-form-item>
    <el-form-item label="ÓÃ»§À´Ô´" prop="sourceType">
      <el-input v-model="dataForm.sourceType" placeholder="ÓÃ»§À´Ô´"></el-input>
    </el-form-item>
    <el-form-item label="»ý·Ö" prop="integration">
      <el-input v-model="dataForm.integration" placeholder="»ý·Ö"></el-input>
    </el-form-item>
    <el-form-item label="³É³¤Öµ" prop="growth">
      <el-input v-model="dataForm.growth" placeholder="³É³¤Öµ"></el-input>
    </el-form-item>
    <el-form-item label="ÆôÓÃ×´Ì¬" prop="status">
      <el-input v-model="dataForm.status" placeholder="ÆôÓÃ×´Ì¬"></el-input>
    </el-form-item>
    <el-form-item label="×¢²áÊ±¼ä" prop="createTime">
      <el-input v-model="dataForm.createTime" placeholder="×¢²áÊ±¼ä"></el-input>
    </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" @click="dataFormSubmit()">确定</el-button>
    </span>
  </el-dialog>
</template>

<script>
  export default {
    data () {
      return {
        visible: false,
        dataForm: {
          id: 0,
          levelId: '',
          username: '',
          password: '',
          nickname: '',
          mobile: '',
          email: '',
          header: '',
          gender: '',
          birth: '',
          city: '',
          job: '',
          sign: '',
          sourceType: '',
          integration: '',
          growth: '',
          status: '',
          createTime: ''
        },
        dataRule: {
          levelId: [
            { required: true, message: '»áÔ±µÈ¼¶id不能为空', trigger: 'blur' }
          ],
          username: [
            { required: true, message: 'ÓÃ»§Ãû不能为空', trigger: 'blur' }
          ],
          password: [
            { required: true, message: 'ÃÜÂë不能为空', trigger: 'blur' }
          ],
          nickname: [
            { required: true, message: 'êÇ³Æ不能为空', trigger: 'blur' }
          ],
          mobile: [
            { required: true, message: 'ÊÖ»úºÅÂë不能为空', trigger: 'blur' }
          ],
          email: [
            { required: true, message: 'ÓÊÏä不能为空', trigger: 'blur' }
          ],
          header: [
            { required: true, message: 'Í·Ïñ不能为空', trigger: 'blur' }
          ],
          gender: [
            { required: true, message: 'ÐÔ±ð不能为空', trigger: 'blur' }
          ],
          birth: [
            { required: true, message: 'ÉúÈÕ不能为空', trigger: 'blur' }
          ],
          city: [
            { required: true, message: 'ËùÔÚ³ÇÊÐ不能为空', trigger: 'blur' }
          ],
          job: [
            { required: true, message: 'Ö°Òµ不能为空', trigger: 'blur' }
          ],
          sign: [
            { required: true, message: '¸öÐÔÇ©Ãû不能为空', trigger: 'blur' }
          ],
          sourceType: [
            { required: true, message: 'ÓÃ»§À´Ô´不能为空', trigger: 'blur' }
          ],
          integration: [
            { required: true, message: '»ý·Ö不能为空', trigger: 'blur' }
          ],
          growth: [
            { required: true, message: '³É³¤Öµ不能为空', trigger: 'blur' }
          ],
          status: [
            { required: true, message: 'ÆôÓÃ×´Ì¬不能为空', trigger: 'blur' }
          ],
          createTime: [
            { required: true, message: '×¢²áÊ±¼ä不能为空', trigger: 'blur' }
          ]
        }
      }
    },
    methods: {
      init (id) {
        this.dataForm.id = id || 0
        this.visible = true
        this.$nextTick(() => {
          this.$refs['dataForm'].resetFields()
          if (this.dataForm.id) {
            this.$http({
              url: this.$http.adornUrl(`/member/member/info/${this.dataForm.id}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.levelId = data.member.levelId
                this.dataForm.username = data.member.username
                this.dataForm.password = data.member.password
                this.dataForm.nickname = data.member.nickname
                this.dataForm.mobile = data.member.mobile
                this.dataForm.email = data.member.email
                this.dataForm.header = data.member.header
                this.dataForm.gender = data.member.gender
                this.dataForm.birth = data.member.birth
                this.dataForm.city = data.member.city
                this.dataForm.job = data.member.job
                this.dataForm.sign = data.member.sign
                this.dataForm.sourceType = data.member.sourceType
                this.dataForm.integration = data.member.integration
                this.dataForm.growth = data.member.growth
                this.dataForm.status = data.member.status
                this.dataForm.createTime = data.member.createTime
              }
            })
          }
        })
      },
      // 表单提交
      dataFormSubmit () {
        this.$refs['dataForm'].validate((valid) => {
          if (valid) {
            this.$http({
              url: this.$http.adornUrl(`/member/member/${!this.dataForm.id ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'id': this.dataForm.id || undefined,
                'levelId': this.dataForm.levelId,
                'username': this.dataForm.username,
                'password': this.dataForm.password,
                'nickname': this.dataForm.nickname,
                'mobile': this.dataForm.mobile,
                'email': this.dataForm.email,
                'header': this.dataForm.header,
                'gender': this.dataForm.gender,
                'birth': this.dataForm.birth,
                'city': this.dataForm.city,
                'job': this.dataForm.job,
                'sign': this.dataForm.sign,
                'sourceType': this.dataForm.sourceType,
                'integration': this.dataForm.integration,
                'growth': this.dataForm.growth,
                'status': this.dataForm.status,
                'createTime': this.dataForm.createTime
              })
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.$message({
                  message: '操作成功',
                  type: 'success',
                  duration: 1500,
                  onClose: () => {
                    this.visible = false
                    this.$emit('refreshDataList')
                  }
                })
              } else {
                this.$message.error(data.msg)
              }
            })
          }
        })
      }
    }
  }
</script>
