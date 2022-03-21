import matplotlib.pyplot as plt
import json
import matplotlib as mpl


def readFile(filename):
    text = ""
    with open(file, "r") as f:
        for line in f:
            text += line
    
    sourcefileDict = json.loads(text)
    return sourcefileDict

file = "./src/test/resources/mesh.json"
jsonDict = readFile(file)
elementList = jsonDict["elements"]
valueList = jsonDict["values"]
nodeList = jsonDict["nodes"]
vertices = {}

valueDict = {}
for item in valueList:
    itemId = item["element_id"]
    value = item["value"]
    valueDict[itemId] = value

# convert nodes
for nodeItem in nodeList:
    nodeId = nodeItem["id"]
    x = nodeItem["x"]
    y = nodeItem["y"]
    vertices[nodeId] = {"x" : x, "y" : y}

valuesWoElementList = [i["value"] for i in valueList]
minValue = min(valuesWoElementList)
maxValue = max(valuesWoElementList)
print(minValue, maxValue)
norm = mpl.colors.Normalize(vmin = minValue, vmax = maxValue)


cmap = mpl.cm.get_cmap('rainbow')
plt.figure()
for polygon in elementList:
    elementId = polygon["id"]
    vertexIds = polygon["nodes"]
    nodeVertices = []
    for vertexId in vertexIds:
        nodeVertices.append([vertices[vertexId]["x"], vertices[vertexId]["y"]])
    nodeVertices.append(nodeVertices[0])
    x = [i[0] for i in nodeVertices]
    y = [i[1] for i in nodeVertices]
    plt.plot(x, y, 'k')

    value = norm(valueDict[elementId])
    rgba = cmap(value)
    pl = plt.Polygon(nodeVertices[:-1], color = rgba)
    plt.gca().add_patch(pl)
plt.show()
