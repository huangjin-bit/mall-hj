<template>
  <div class="spu-form-container">
    <el-card>
      <el-steps :active="currentStep" align-center finish-status="success">
        <el-step title="基本信息" />
        <el-step title="基本属性" />
        <el-step title="SKU信息" />
        <el-step title="确认提交" />
      </el-steps>

      <div class="step-content mt-20">
        <!-- 步骤1: 基本信息 -->
        <div v-show="currentStep === 0">
          <el-form ref="basicFormRef" :model="basicForm" :rules="basicRules" label-width="120px">
            <el-form-item label="SPU名称" prop="spuName">
              <el-input v-model="basicForm.spuName" placeholder="请输入SPU名称" />
            </el-form-item>
            <el-form-item label="商品分类" prop="categoryId">
              <TreeSelect v-model="basicForm.categoryId" placeholder="请选择分类" />
            </el-form-item>
            <el-form-item label="商品品牌" prop="brandId">
              <el-select v-model="basicForm.brandId" placeholder="请选择品牌">
                <el-option
                  v-for="brand in brandList"
                  :key="brand.id"
                  :label="brand.name"
                  :value="brand.id"
                />
              </el-select>
            </el-form-item>
            <el-form-item label="商品重量(g)" prop="weight">
              <el-input-number v-model="basicForm.weight" :min="0" />
            </el-form-item>
            <el-form-item label="商品描述" prop="description">
              <el-input
                v-model="basicForm.description"
                type="textarea"
                :rows="4"
                placeholder="请输入商品描述"
              />
            </el-form-item>
            <el-form-item label="商品图片" prop="images">
              <ImageUpload v-model="basicForm.images" :limit="9" />
            </el-form-item>
          </el-form>
        </div>

        <!-- 步骤2: 基本属性 -->
        <div v-show="currentStep === 1">
          <el-alert
            v-if="!basicForm.categoryId"
            title="请先选择商品分类"
            type="warning"
            :closable="false"
            show-icon
          />
          <div v-else>
            <div v-for="group in attrGroups" :key="group.id" class="attr-group-item">
              <h4>{{ group.groupName }}</h4>
              <el-form label-width="120px">
                <el-form-item
                  v-for="attr in group.attrs"
                  :key="attr.id"
                  :label="attr.attrName"
                >
                  <el-input
                    v-if="attr.valueType === 1"
                    v-model="baseAttrs[attr.id!]"
                    :placeholder="`请输入${attr.attrName}`"
                  />
                  <el-select
                    v-else
                    v-model="baseAttrs[attr.id!]"
                    :placeholder="`请选择${attr.attrName}`"
                  >
                    <el-option
                      v-for="val in getAttrValues(attr)"
                      :key="val"
                      :label="val"
                      :value="val"
                    />
                  </el-select>
                </el-form-item>
              </el-form>
            </div>
          </div>
        </div>

        <!-- 步骤3: SKU信息 -->
        <div v-show="currentStep === 2">
          <el-alert
            v-if="!basicForm.categoryId"
            title="请先选择商品分类"
            type="warning"
            :closable="false"
            show-icon
          />
          <div v-else>
            <el-button type="primary" @click="handleAddSku" class="mb-20">添加SKU</el-button>
            <el-table :data="skuList" border>
              <el-table-column label="SKU名称" width="200">
                <template #default="{ row, $index }">
                  <el-input v-model="row.skuName" placeholder="请输入SKU名称" />
                </template>
              </el-table-column>
              <el-table-column label="销售属性">
                <template #default="{ row }">
                  <el-tag
                    v-for="(val, key) in row.saleAttrs"
                    :key="key"
                    class="mr-10"
                  >
                    {{ key }}: {{ val }}
                  </el-tag>
                  <el-button type="primary" link @click="handleEditSaleAttrs(row)">设置</el-button>
                </template>
              </el-table-column>
              <el-table-column label="价格" width="150">
                <template #default="{ row }">
                  <el-input-number v-model="row.price" :min="0" :precision="2" />
                </template>
              </el-table-column>
              <el-table-column label="原价" width="150">
                <template #default="{ row }">
                  <el-input-number v-model="row.originalPrice" :min="0" :precision="2" />
                </template>
              </el-table-column>
              <el-table-column label="库存" width="120">
                <template #default="{ row }">
                  <el-input-number v-model="row.stock" :min="0" />
                </template>
              </el-table-column>
              <el-table-column label="操作" width="100">
                <template #default="{ $index }">
                  <el-button type="danger" link @click="handleRemoveSku($index)">删除</el-button>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </div>

        <!-- 步骤4: 确认提交 -->
        <div v-show="currentStep === 3">
          <el-descriptions title="基本信息" :column="2" border>
            <el-descriptions-item label="SPU名称">{{ basicForm.spuName }}</el-descriptions-item>
            <el-descriptions-item label="商品品牌">{{ getBrandName() }}</el-descriptions-item>
            <el-descriptions-item label="商品重量">{{ basicForm.weight }}g</el-descriptions-item>
            <el-descriptions-item label="商品描述">{{ basicForm.description }}</el-descriptions-item>
            <el-descriptions-item label="商品图片" :span="2">
              <div class="image-preview">
                <el-image
                  v-for="(img, index) in getImageList()"
                  :key="index"
                  :src="img"
                  style="width: 100px; height: 100px; margin-right: 10px"
                  fit="cover"
                />
              </div>
            </el-descriptions-item>
          </el-descriptions>

          <el-divider />

          <h4>SKU列表</h4>
          <el-table :data="skuList" border>
            <el-table-column prop="skuName" label="SKU名称" />
            <el-table-column label="销售属性">
              <template #default="{ row }">
                <el-tag
                  v-for="(val, key) in row.saleAttrs"
                  :key="key"
                  class="mr-10"
                >
                  {{ key }}: {{ val }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="price" label="价格" />
            <el-table-column prop="originalPrice" label="原价" />
            <el-table-column prop="stock" label="库存" />
          </el-table>
        </div>
      </div>

      <div class="step-actions mt-20">
        <el-button v-if="currentStep > 0" @click="currentStep--">上一步</el-button>
        <el-button
          v-if="currentStep < 3"
          type="primary"
          @click="handleNextStep"
        >
          下一步
        </el-button>
        <el-button
          v-if="currentStep === 3"
          type="primary"
          @click="handleSubmit"
          :loading="submitting"
        >
          提交
        </el-button>
      </div>
    </el-card>

    <!-- 销售属性设置对话框 -->
    <el-dialog v-model="saleAttrDialogVisible" title="设置销售属性" width="600px">
      <el-form label-width="120px">
        <el-form-item
          v-for="attr in saleAttrs"
          :key="attr.id"
          :label="attr.attrName"
        >
          <el-select v-model="tempSaleAttrs[attr.id!]" :placeholder="`请选择${attr.attrName}`">
            <el-option
              v-for="val in getAttrValues(attr)"
              :key="val"
              :label="val"
              :value="val"
            />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="saleAttrDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleConfirmSaleAttrs">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, FormInstance, FormRules } from 'element-plus'
import {
  getSpuDetail,
  saveSpu,
  updateSpu,
  getGroupsWithAttrs,
  getSaleAttrs
} from '@/api/product'
import { getBrandList } from '@/api/product/brand'
import TreeSelect from '@/components/TreeSelect.vue'
import ImageUpload from '@/components/ImageUpload.vue'

const route = useRoute()
const router = useRouter()

const currentStep = ref(0)
const submitting = ref(false)
const isEdit = ref(!!route.params.id)
const spuId = ref<number>(Number(route.params.id))

const basicFormRef = ref<FormInstance>()
const saleAttrDialogVisible = ref(false)
const currentSkuIndex = ref<number>()
const tempSaleAttrs = ref<Record<number, string>>({})

const brandList = ref<any[]>([])
const attrGroups = ref<any[]>([])
const saleAttrs = ref<any[]>([])
const baseAttrs = ref<Record<number, string>>({})

const basicForm = reactive({
  spuName: '',
  categoryId: undefined as number | undefined,
  brandId: undefined as number | undefined,
  weight: 0,
  description: '',
  images: [] as string[]
})

const basicRules: FormRules = {
  spuName: [{ required: true, message: '请输入SPU名称', trigger: 'blur' }],
  categoryId: [{ required: true, message: '请选择分类', trigger: 'change' }],
  brandId: [{ required: true, message: '请选择品牌', trigger: 'change' }]
}

const skuList = ref<any[]>([])

onMounted(async () => {
  await loadBrands()
  if (isEdit.value) {
    await loadSpuDetail()
  }
})

const loadBrands = async () => {
  try {
    const { records } = await getBrandList({ current: 1, size: 100 })
    brandList.value = records
  } catch (error) {
    console.error('[SPU Form] Failed to load brands:', error)
  }
}

const loadSpuDetail = async () => {
  try {
    const data = await getSpuDetail(spuId.value)
    Object.assign(basicForm, data)
    if (typeof data.images === 'string') {
      basicForm.images = data.images.split(',')
    }
  } catch (error) {
    console.error('[SPU Form] Failed to load SPU detail:', error)
  }
}

const getBrandName = () => {
  const brand = brandList.value.find(b => b.id === basicForm.brandId)
  return brand?.name || ''
}

const getImageList = () => {
  if (typeof basicForm.images === 'string') {
    return basicForm.images.split(',').filter((i: string) => i)
  }
  return basicForm.images
}

const getAttrValues = (attr: any) => {
  return attr.valueSelect ? attr.valueSelect.split(',') : []
}

const handleNextStep = async () => {
  if (currentStep.value === 0) {
    if (!basicFormRef.value) return
    await basicFormRef.value.validate(async (valid) => {
      if (valid) {
        await loadAttrGroups()
        currentStep.value++
      }
    })
  } else if (currentStep.value === 1) {
    await loadSaleAttrs()
    currentStep.value++
  } else {
    currentStep.value++
  }
}

const loadAttrGroups = async () => {
  try {
    const data = await getGroupsWithAttrs(basicForm.categoryId!)
    attrGroups.value = data
  } catch (error) {
    console.error('[SPU Form] Failed to load attr groups:', error)
  }
}

const loadSaleAttrs = async () => {
  try {
    const { records } = await getSaleAttrs(basicForm.categoryId!)
    saleAttrs.value = records
  } catch (error) {
    console.error('[SPU Form] Failed to load sale attrs:', error)
  }
}

const handleAddSku = () => {
  skuList.value.push({
    skuName: '',
    price: 0,
    originalPrice: 0,
    stock: 0,
    saleAttrs: {}
  })
}

const handleRemoveSku = (index: number) => {
  skuList.value.splice(index, 1)
}

const handleEditSaleAttrs = (row: any) => {
  currentSkuIndex.value = skuList.value.indexOf(row)
  tempSaleAttrs.value = { ...row.saleAttrs }
  saleAttrDialogVisible.value = true
}

const handleConfirmSaleAttrs = () => {
  if (currentSkuIndex.value !== undefined) {
    skuList.value[currentSkuIndex.value].saleAttrs = { ...tempSaleAttrs.value }
  }
  saleAttrDialogVisible.value = false
}

const handleSubmit = async () => {
  submitting.value = true
  try {
    const data = {
      ...basicForm,
      images: getImageList().join(','),
      baseAttrs,
      skus: skuList.value
    }

    if (isEdit.value) {
      await updateSpu({ ...data, id: spuId.value })
      ElMessage.success('更新成功')
    } else {
      await saveSpu(data)
      ElMessage.success('保存成功')
    }
    router.push('/product/spu')
  } catch (error) {
    console.error('[SPU Form] Submit failed:', error)
  } finally {
    submitting.value = false
  }
}
</script>

<style scoped lang="scss">
.spu-form-container {
  .step-content {
    min-height: 400px;
  }

  .attr-group-item {
    margin-bottom: 20px;
    padding: 15px;
    background-color: #f5f7fa;
    border-radius: 4px;

    h4 {
      margin: 0 0 15px 0;
      color: #303133;
    }
  }

  .image-preview {
    display: flex;
    flex-wrap: wrap;
  }

  .step-actions {
    text-align: center;
    border-top: 1px solid #e6e6e6;
    padding-top: 20px;
  }
}
</style>
