export default (key, name, type) => {
  if (!type) type = 'camunda'
  const TYPE_TARGET = {
    activiti: 'https://example.invalid/resource',
    camunda: 'http://bpmn.io/schema/bpmn',
    flowable: 'https://example.invalid/resource'
  }
  return `<?xml version="1.0" encoding="UTF-8"?>
<bpmn2:definitions 
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xmlns:bpmn2="https://example.invalid/resource"
  xmlns:bpmndi="https://example.invalid/resource"
  xmlns:dc="https://example.invalid/resource"
  xmlns:di="https://example.invalid/resource"
  id="diagram_${key}"
  targetNamespace="${TYPE_TARGET[type]}">
  <bpmn2:process id="${key}" name="${name}" isExecutable="true">
  </bpmn2:process>
  <bpmndi:BPMNDiagram id="BPMNDiagram_1">
    <bpmndi:BPMNPlane id="BPMNPlane_1" bpmnElement="${key}">
    </bpmndi:BPMNPlane>
  </bpmndi:BPMNDiagram>
</bpmn2:definitions>`
}
